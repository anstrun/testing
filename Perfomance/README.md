# Prueba de Carga — Servicio de Login (k6)

Script de prueba de carga sobre el servicio de login `POST /auth/login` de
[fakestoreapi](https://fakestoreapi.com), implementado con **k6**. Las
credenciales se parametrizan desde un archivo CSV y el escenario por defecto
reproduce un perfil de carga sostenida (rampa → meseta ~140 VUs → bajada) de
aproximadamente 1 hora.

## Criterios de aceptación

| Criterio | Objetivo | Threshold en k6 |
|---|---|---|
| Throughput | ≥ 20 TPS | `http_reqs: rate>20` |
| Tiempo de respuesta | máx. 1,5 s | `http_req_duration: p(95)<1500` |
| Tasa de error | < 3% | `http_req_failed: rate<0.03` |

## Estructura

```
k6-login-loadtest/
├── scripts/
│   └── login_load_test.js     # script principal
├── data/
│   └── users.csv              # credenciales (username,password)
├── reports/
│   ├── summary.json           # (generado) resumen JSON
│   └── textSummary.txt        # (generado) resumen en texto
├── results/                   # salidas adicionales
├── readme.txt                 # instrucciones detalladas
├── conclusiones.txt           # hallazgos y conclusiones
└── README.md
```

## Requisitos

- **k6** v0.50.0 o superior — https://k6.io
- Conexión a internet (las librerías `papaparse` y `k6-summary` se cargan desde
  jslib.k6.io en tiempo de ejecución).

No requiere Node.js ni npm para ejecutar.

## Uso

```bash
# Prueba de humo (validación rápida)
k6 run -e PROFILE=smoke scripts/login_load_test.js

# Carga media (valida los 20 TPS en pocos minutos)
k6 run -e PROFILE=load scripts/login_load_test.js

# Carga sostenida ~1h (perfil del informe, por defecto)
k6 run scripts/login_load_test.js
```

### Parámetros (`-e`)

| Variable | Default | Descripción |
|---|---|---|
| `BASE_URL` | `https://fakestoreapi.com` | URL base del servicio |
| `LOGIN_PATH` | `/auth/login` | Ruta del endpoint de login |
| `PROFILE` | `soak` | `smoke` \| `load` \| `soak` |
| `TESTID` | `login-<PROFILE>` | Etiqueta de la corrida |

## Salida con métricas de series temporales (opcional)

Para generar el gráfico VUs vs req/s posteriormente, exporta el JSON de muestras:

```bash
k6 run --out json=results/metrics.json scripts/login_load_test.js
python3 scripts/plot_vus_vs_reqs.py results/metrics.json results/vus_vs_reqs.png
```

## Resultados y conclusiones

Ver [`conclusiones.txt`](conclusiones.txt). En la corrida de referencia el
sistema cumplió throughput (73,18 TPS) y tasa de error (2,44%), pero **no** el
tiempo de respuesta (p95 = 1,57 s > 1,5 s), con una degradación temporal del
backend (errores HTTP 5xx) bajo concurrencia sostenida.
