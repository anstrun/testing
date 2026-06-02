import http from 'k6/http';
import { check, fail } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';
import { SharedArray } from 'k6/data';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

/* =========================================================================
 *  Prueba de carga - Servicio de login (POST /auth/login)
 *  Ejercicio 1 - Script de Pruebas de Carga
 *
 *  Objetivos de aceptacion:
 *    - Alcanzar al menos 20 TPS.
 *    - Tiempo de respuesta maximo permitido: 1,5 s.
 *    - Tasa de error aceptable: < 3% del total de peticiones.
 *
 *  El escenario por defecto reproduce un perfil de carga sostenida
 *  (rampa -> meseta ~140 VUs -> bajada) de aproximadamente 1 hora, en
 *  linea con el InformeResultados analizado. Es configurable por ENV.
 * ========================================================================= */

// ----------------------------- Configuracion -----------------------------
const BASE_URL = __ENV.BASE_URL || 'https://fakestoreapi.com';
const LOGIN_PATH = __ENV.LOGIN_PATH || '/auth/login';

// Perfil de carga seleccionable: 'soak' (1h, ~140 VUs), 'smoke', 'load'
const PROFILE = (__ENV.PROFILE || 'soak').toLowerCase();

// --------------------------- Metricas custom ------------------------------
const loginDuration = new Trend('login_duration', true);
const loginFailRate = new Rate('login_failed');
const tokenReceived = new Counter('login_token_received');

// ------------------------ Carga de datos (CSV) ----------------------------
// SharedArray => se carga UNA vez y se comparte entre todos los VUs (memoria).
const users = new SharedArray('users', function () {
  return papaparse.parse(open('../data/users.csv'), { header: true })
    .data
    // Filtra filas vacias que papaparse puede dejar al final del archivo.
    .filter((u) => u && u.username && u.password);
});

// ---------------------------- Escenarios ----------------------------------
const profiles = {
  // Validacion rapida: 1 VU, pocas iteraciones. Ideal para CI / humo.
  smoke: {
    executor: 'shared-iterations',
    vus: 1,
    iterations: 10,
    maxDuration: '1m',
  },

  // Carga media para validar el umbral de 20 TPS en poco tiempo.
  load: {
    executor: 'ramping-vus',
    startVUs: 0,
    stages: [
      { duration: '1m', target: 40 },
      { duration: '3m', target: 40 },
      { duration: '1m', target: 0 },
    ],
    gracefulRampDown: '30s',
  },

  // Carga sostenida ~1h con meseta cerca de 140 VUs (perfil del informe).
  soak: {
    executor: 'ramping-vus',
    startVUs: 0,
    stages: [
      { duration: '2m', target: 140 },   // rampa de subida
      { duration: '56m', target: 140 },  // meseta sostenida
      { duration: '2m', target: 0 },     // bajada
    ],
    gracefulRampDown: '30s',
  },
};

if (!profiles[PROFILE]) {
  throw new Error(`PROFILE invalido: "${PROFILE}". Use smoke | load | soak`);
}

// ------------------------------ Opciones ----------------------------------
export const options = {
  scenarios: {
    login_scenario: profiles[PROFILE],
  },
  thresholds: {
    // Tasa de error aceptable: menor al 3% del total de peticiones.
    http_req_failed: ['rate<0.03'],
    login_failed: ['rate<0.03'],
    // Tiempo de respuesta permitido: maximo 1,5 s. Se valida sobre p95.
    http_req_duration: ['p(95)<1500'],
    login_duration: ['p(95)<1500'],
    // Verificacion del objetivo de >= 20 TPS (peticiones totales / duracion).
    http_reqs: ['rate>20'],
  },
  // Resumen con todos los percentiles relevantes.
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
  // Etiqueta para distinguir corridas en herramientas externas.
  tags: { testid: __ENV.TESTID || `login-${PROFILE}` },
};

// ------------------------------ Test logic --------------------------------
export default function () {
  // Selecciona credenciales de forma pseudoaleatoria entre las del CSV.
  const cred = users[Math.floor(Math.random() * users.length)];

  const payload = JSON.stringify({
    username: cred.username,
    password: cred.password,
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
    timeout: '60s', // equivalente a --max-time 60 del CURL del ejercicio.
    tags: { name: 'POST /auth/login' },
  };

  const res = http.post(`${BASE_URL}${LOGIN_PATH}`, payload, params);

  // Metricas custom del login.
  loginDuration.add(res.timings.duration);

  // Validaciones del ejercicio.
  let token = null;
  try {
    token = res.json('token');
  } catch (e) {
    token = null;
  }

  const ok = check(res, {
    'status es 200 (o 201)': (r) => r.status === 200 || r.status === 201,
    'respuesta < 1,5 s': (r) => r.timings.duration < 1500,
    'devuelve token': () => typeof token === 'string' && token.length > 0,
  });

  if (token) {
    tokenReceived.add(1);
  }

  // login_failed = true cuando NO se cumple el contrato de login exitoso.
  loginFailRate.add(!ok);
}

// ------------------------- Reportes de salida -----------------------------
export function handleSummary(data) {
  return {
    'reports/summary.json': JSON.stringify(data, null, 2),
    'reports/textSummary.txt': textSummary(data, { indent: ' ', enableColors: false }),
    stdout: textSummary(data, { indent: ' ', enableColors: true }),
  };
}
