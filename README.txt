========================================================================
PRUEBA TECNICA QA - AUTOMATIZACION DE PRUEBAS
========================================================================

Este paquete contiene DOS ejercicios independientes. Cada uno es un
proyecto Maven completo, listo para subir a su propio repositorio
GitHub publico (o ambos al mismo repo en carpetas separadas).


CONTENIDO
---------
  ejercicio1-serenity/   -> Prueba E2E del flujo de compra en SauceDemo
                            usando Serenity BDD + Screenplay + Cucumber.

  ejercicio2-karate/     -> Pruebas REST sobre la API de la PetStore
                            usando Karate.

Cada carpeta trae su propio:
  - readme.txt        (instrucciones paso a paso de ejecucion)
  - conclusiones.txt  (hallazgos y conclusiones)
  - pom.xml           (configuracion Maven)
  - codigo fuente comentado linea por linea
  - .gitignore


REQUISITOS GENERALES
--------------------
  - Java JDK 11 o superior
  - Maven 3.6 o superior
  - Conexion a internet
  - (Solo ejercicio 1) Google Chrome instalado


COMO EMPEZAR
------------
  Entra a la carpeta del ejercicio que quieras y sigue su readme.txt:

    Ejercicio 1:  cd ejercicio1-serenity  &&  mvn clean verify
    Ejercicio 2:  cd ejercicio2-karate    &&  mvn clean test


NOTA SOBRE LOS COMENTARIOS
--------------------------
El codigo esta comentado en un tono coloquial ecuatoriano (guayaco)
a pedido, para explicar QUE hace cada linea de forma cercana. La
logica y la estructura siguen las buenas practicas estandar de cada
framework.
