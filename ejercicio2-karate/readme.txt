========================================================================
EJERCICIO 2 - PRUEBAS REST PETSTORE CON KARATE
========================================================================

DESCRIPCION
-----------
Pruebas automatizadas de servicios REST sobre la API de la PetStore
(https://petstore.swagger.io/) usando la herramienta Karate.

Casos cubiertos (todos en un mismo escenario, compartiendo el petId):
  1. Anadir una mascota a la tienda            -> POST /pet
  2. Consultar la mascota por ID               -> GET  /pet/{id}
  3. Actualizar nombre y estatus a "sold"      -> PUT  /pet
  4. Consultar la mascota por estatus "sold"   -> GET  /pet/findByStatus?status=sold


REQUISITOS PREVIOS
------------------
  - Java JDK 11 o superior   (verificar con: java -version)
  - Maven 3.6 o superior      (verificar con: mvn -version)
  - Conexion a internet (la API esta en internet)


ESTRUCTURA DEL PROYECTO
-----------------------
  pom.xml                                  -> Maven y dependencia de Karate
  src/test/java/
      karate-config.js                     -> configuracion global (URL base)
      logback-test.xml                     -> formato de los logs
      petstore/
          petstore.feature                 -> los 4 casos de prueba REST
          PetStoreRunner.java              -> clase que arranca las pruebas


PASOS PARA EJECUTAR
-------------------
  1. Abrir una terminal en la carpeta del proyecto (donde esta el pom.xml).

  2. Ejecutar:

        mvn clean test

     Esto descarga Karate, corre las 4 pruebas y genera el reporte HTML.

  3. (Opcional) Correr solo la feature directamente:

        mvn test -Dtest=PetStoreRunner


DONDE VER EL REPORTE
--------------------
  Despues de ejecutar, Karate genera un reporte en:

        target/karate-reports/karate-summary.html

  Abrelo en el navegador. Ahi se ve cada peticion, el request, la
  respuesta JSON y si cada validacion paso o fallo.


NOTAS IMPORTANTES
-----------------
  - El petId se genera con la hora actual en milisegundos, asi cada corrida
    usa una mascota distinta y la prueba es repetible sin chocar.

  - La PetStore de Swagger es un entorno de demostracion compartido y a
    veces tarda o devuelve datos en cache. Si un caso falla por eso,
    volver a correr "mvn clean test" suele resolverlo.


SUBIR A GITHUB
--------------
  git init
  git add .
  git commit -m "Ejercicio 2: Pruebas REST PetStore con Karate"
  git branch -M main
  git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
  git push -u origin main

  IMPORTANTE: no subir la carpeta target/ (ver .gitignore).
