===============================================================================
 PRUEBA DE CARGA - SERVICIO DE LOGIN (fakestoreapi /auth/login)
 Ejercicio 1 - Script de Pruebas de Carga
===============================================================================

-------------------------------------------------------------------------------
1. DESCRIPCION
-------------------------------------------------------------------------------
Script de prueba de carga construido con k6 que ejercita el servicio de login
(POST /auth/login). Las credenciales se parametrizan desde un archivo CSV. El
escenario por defecto reproduce un perfil de carga sostenida (rampa -> meseta
de ~140 usuarios virtuales -> bajada) de aproximadamente 1 hora, coherente con
el InformeResultados analizado.

Criterios de aceptacion validados automaticamente (thresholds de k6):
  - Throughput: al menos 20 TPS              -> http_reqs rate > 20
  - Tiempo de respuesta maximo: 1,5 s        -> http_req_duration p(95) < 1500 ms
  - Tasa de error aceptable: menor al 3%     -> http_req_failed rate < 0.03

-------------------------------------------------------------------------------
2. VERSIONES DE TECNOLOGIAS USADAS
-------------------------------------------------------------------------------
  - k6                : v0.50.0 o superior   (https://k6.io)
  - Sistema operativo : Linux / macOS / Windows (cualquiera soportado por k6)
  - Librerias jslib k6 (se descargan en tiempo de ejecucion, requiere internet):
      * papaparse 5.1.1     -> parseo del CSV de credenciales
      * k6-summary 0.0.2    -> generacion de textSummary.txt

  Nota: no se requiere Node.js ni npm para ejecutar. k6 trae su propio runtime.

-------------------------------------------------------------------------------
3. ESTRUCTURA DEL PROYECTO
-------------------------------------------------------------------------------
  k6-login-loadtest/
  |- scripts/
  |   |- login_load_test.js     <- script principal de la prueba
  |- data/
  |   |- users.csv              <- credenciales parametrizadas (username,password)
  |- reports/
  |   |- summary.json           <- (generado) resumen completo en JSON
  |   |- textSummary.txt        <- (generado) resumen en texto plano
  |- results/                   <- (opcional) salidas adicionales / capturas
  |- readme.txt                 <- este archivo
  |- conclusiones.txt           <- hallazgos y conclusiones del ejercicio

-------------------------------------------------------------------------------
4. INSTALACION DE k6
-------------------------------------------------------------------------------
  Linux (Debian/Ubuntu):
    sudo gpg -k
    sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg \
        --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
    echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" \
        | sudo tee /etc/apt/sources.list.d/k6.list
    sudo apt-get update
    sudo apt-get install k6

  macOS (Homebrew):
    brew install k6

  Windows (Chocolatey):
    choco install k6

  Verificar instalacion:
    k6 version

-------------------------------------------------------------------------------
5. EJECUCION PASO A PASO
-------------------------------------------------------------------------------
  PASO 1. Clonar / descargar el repositorio y ubicarse en la raiz del proyecto:
      cd k6-login-loadtest

  PASO 2. (Opcional) Ejecutar primero una prueba de humo para validar todo:
      k6 run -e PROFILE=smoke scripts/login_load_test.js

  PASO 3. Ejecutar el escenario de carga media (rapido, valida 20 TPS):
      k6 run -e PROFILE=load scripts/login_load_test.js

  PASO 4. Ejecutar el escenario de carga sostenida (~1 hora, perfil del informe):
      k6 run -e PROFILE=soak scripts/login_load_test.js

      Es el escenario por defecto, por lo que tambien funciona:
      k6 run scripts/login_load_test.js

  Al finalizar, k6 imprime el resumen en consola y genera:
      reports/summary.json
      reports/textSummary.txt

-------------------------------------------------------------------------------
6. PARAMETROS CONFIGURABLES (variables de entorno -e)
-------------------------------------------------------------------------------
  BASE_URL    URL base del servicio.   Default: https://fakestoreapi.com
  LOGIN_PATH  Ruta del login.          Default: /auth/login
  PROFILE     Perfil de carga.         Valores: smoke | load | soak  (def: soak)
  TESTID      Etiqueta de la corrida.  Default: login-<PROFILE>

  Ejemplos:
    k6 run -e BASE_URL=https://mi-servidor.com -e PROFILE=load scripts/login_load_test.js
    k6 run -e PROFILE=soak -e TESTID=corrida-prod scripts/login_load_test.js

-------------------------------------------------------------------------------
7. PARAMETRIZACION DE DATOS (CSV)
-------------------------------------------------------------------------------
  El archivo data/users.csv tiene cabecera y una fila por credencial:

      username,password
      donero,ewedon
      kevinryan,kev02937@
      johnd,m38rmF$
      derek,jklg*_56
      mor_2314,83r5^_

  El script carga el CSV una sola vez con SharedArray (eficiente en memoria) y
  cada iteracion elige una credencial de forma pseudoaleatoria. Para agregar
  mas usuarios basta con anadir filas al CSV; no se toca el script.

-------------------------------------------------------------------------------
8. INTERPRETACION DE RESULTADOS
-------------------------------------------------------------------------------
  - Si los thresholds se cumplen, k6 termina con codigo de salida 0.
  - Si algun threshold falla (p95 > 1,5 s, error >= 3% o TPS < 20), k6 termina
    con codigo de salida 99 y marca el threshold en rojo en el resumen.
  - Las metricas clave a revisar: http_req_duration (p95, max), http_req_failed
    (rate), http_reqs (rate = TPS) y los checks de status/token.
===============================================================================
