========================================================================
EJERCICIO 1 - PRUEBA E2E SAUCEDEMO CON SERENITY BDD
========================================================================

DESCRIPCION
-----------
Prueba funcional automatizada (E2E) del flujo de compra en
https://www.saucedemo.com/ usando Serenity BDD + Screenplay + Cucumber.

El flujo cubre:
  1. Autenticarse con el usuario standard_user / secret_sauce
  2. Agregar dos productos al carrito
  3. Visualizar el carrito
  4. Completar el formulario de compra
  5. Finalizar la compra hasta la confirmacion "THANK YOU FOR YOUR ORDER"


REQUISITOS PREVIOS
------------------
  - Java JDK 11 o superior   (verificar con: java -version)
  - Maven 3.6 o superior      (verificar con: mvn -version)
  - Google Chrome instalado   (Serenity baja el driver automaticamente)
  - Conexion a internet (pa' bajar dependencias y abrir la pagina)


ESTRUCTURA DEL PROYECTO
-----------------------
  pom.xml                              -> configuracion de Maven y dependencias
  src/test/resources/
      features/saucedemo/compra.feature  -> el escenario en Gherkin (espanol)
      serenity.conf                      -> configuracion del navegador
  src/test/java/saucedemo/
      userinterface/   -> Page Objects (selectores de cada pantalla)
      stepdefinitions/ -> traduccion de los pasos Gherkin a codigo
      runner/          -> clase que arranca las pruebas


PASOS PARA EJECUTAR
-------------------
  1. Abrir una terminal dentro de la carpeta del proyecto (donde esta el pom.xml).

  2. Ejecutar el comando:

        mvn clean verify

     Esto descarga dependencias, corre la prueba y genera el reporte.

  3. (Opcional) Si quieres ver el navegador mientras corre, edita
     src/test/resources/serenity.conf y pon:

        headless.mode = false


DONDE VER EL REPORTE
--------------------
  Despues de ejecutar, abrir en el navegador:

        target/site/serenity/index.html

  Ahi se ven los pasos, las capturas de pantalla y si la prueba paso o fallo.


COMANDOS UTILES
---------------
  mvn clean verify      -> limpia, corre las pruebas y genera reporte
  mvn clean             -> borra la carpeta target (resultados anteriores)


SUBIR A GITHUB
--------------
  git init
  git add .
  git commit -m "Ejercicio 1: E2E SauceDemo con Serenity BDD"
  git branch -M main
  git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
  git push -u origin main

  IMPORTANTE: no subir la carpeta target/ (ver .gitignore).
