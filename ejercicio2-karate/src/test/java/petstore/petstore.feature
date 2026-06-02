# =============================================================================
# petstore.feature
# Aqui van las pruebas de la API REST de la PetStore (Swagger).
# Karate usa Gherkin (Feature / Scenario / Given / When / Then) pero cada
# paso ya tiene poder real: hace peticiones HTTP y valida respuestas JSON.
# Los 4 casos van en UN SOLO Scenario porque comparten el mismo petId
# (la mascota que creamos al inicio se usa en todos los pasos siguientes).
# =============================================================================

Feature: Pruebas REST sobre la API de la PetStore

  # Background: corre antes del escenario. Aqui dejamos lista la URL base.
  Background:
    # Tomamos la baseUrl que definimos en karate-config.js
    * url baseUrl

  Scenario: Ciclo completo de una mascota - crear, consultar, actualizar y filtrar

    # =====================================================================
    # CASO 1: ANADIR UNA MASCOTA A LA TIENDA  (POST /pet)
    # =====================================================================

    # Generamos un id unico usando la hora actual en milisegundos.
    # Asi cada corrida usa una mascota distinta y no choca con otras pruebas.
    * def petId = '' + Math.floor(Date.now())

    # Armamos el cuerpo (body) de la mascota en formato JSON.
    # Este es el dato de ENTRADA que vamos a enviar.
    * def nuevaMascota =
    """
    {
      "id": #(petId),
      "category": { "id": 1, "name": "perros" },
      "name": "Firulais",
      "photoUrls": ["https://foto.ejemplo/firulais.jpg"],
      "tags": [ { "id": 1, "name": "guardian" } ],
      "status": "available"
    }
    """

    # Le decimos a Karate que el cuerpo de la peticion es esa mascota
    Given path 'pet'
    And request nuevaMascota
    # Hacemos la peticion POST pa' crear la mascota
    When method POST
    # VALIDACION (test): el servidor debe responder 200 (creado OK)
    Then status 200
    # Validamos que el nombre que volvio sea el mismo que mandamos
    And match response.name == 'Firulais'
    # Validamos que el status sea "available"
    And match response.status == 'available'
    # Guardamos el id que devolvio el servidor (CAPTURA DE SALIDA) por si acaso
    * def idCreado = response.id
    # Imprimimos en consola pa' tener evidencia de qué id se creó
    * print 'Mascota creada con id:', idCreado

    # =====================================================================
    # CASO 2: CONSULTAR LA MASCOTA INGRESADA  (GET /pet/{id})
    # =====================================================================

    # Apuntamos a /pet/{petId} usando el id que creamos
    Given path 'pet', petId
    # Hacemos la peticion GET pa' consultarla
    When method GET
    # VALIDACION: debe responder 200 (la encontro)
    Then status 200
    # El id que vuelve debe coincidir con el que buscamos.
    # Convertimos ambos a texto pa' comparar sin lios de tipo numerico.
    And match ('' + response.id) == petId
    # El nombre debe seguir siendo Firulais
    And match response.name == 'Firulais'
    * print 'Mascota consultada correctamente:', response.name

    # =====================================================================
    # CASO 3: ACTUALIZAR NOMBRE Y ESTATUS A "sold"  (PUT /pet)
    # =====================================================================

    # Armamos el nuevo cuerpo con el nombre cambiado y status "sold"
    * def mascotaActualizada =
    """
    {
      "id": #(petId),
      "category": { "id": 1, "name": "perros" },
      "name": "Firulais Modificado",
      "photoUrls": ["https://foto.ejemplo/firulais.jpg"],
      "tags": [ { "id": 1, "name": "guardian" } ],
      "status": "sold"
    }
    """

    Given path 'pet'
    And request mascotaActualizada
    # PUT es el metodo pa' actualizar un recurso existente
    When method PUT
    # VALIDACION: 200 OK
    Then status 200
    # El nombre debe haber cambiado
    And match response.name == 'Firulais Modificado'
    # El status ahora debe ser "sold" (vendida)
    And match response.status == 'sold'
    * print 'Mascota actualizada. Nuevo status:', response.status

    # =====================================================================
    # CASO 4: CONSULTAR POR ESTATUS  (GET /pet/findByStatus?status=sold)
    # =====================================================================

    Given path 'pet', 'findByStatus'
    # Le pasamos el parametro de busqueda ?status=sold
    And param status = 'sold'
    When method GET
    # VALIDACION: 200 OK
    Then status 200
    # La respuesta debe ser una lista (array) de mascotas
    And match response == '#[_ > 0]'
    # Validamos que NUESTRA mascota (la del petId) este dentro de la lista devuelta.
    # Filtramos la lista buscando el id que creamos.
    * def encontrada = karate.filter(response, function(x){ return '' + x.id == petId })
    # Debe haber encontrado al menos una coincidencia
    And match encontrada == '#[1]'
    # Y esa mascota encontrada debe tener status "sold"
    And match encontrada[0].status == 'sold'
    * print 'Mascota encontrada por estatus sold. Total en la lista:', response.length
