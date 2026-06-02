# language: es
# =============================================================================
# Este archivo .feature está escrito en Gherkin (idioma de Cucumber).
# Le pusimos "# language: es" pa' poder escribir en español: Dado, Cuando, Entonces.
# Aquí se cuenta en palabras humanas QUÉ debe hacer la prueba, sin código todavía.
# =============================================================================

@compra
Característica: Flujo de compra E2E en SauceDemo
  Como cliente de la tienda SauceDemo
  Quiero comprar un par de productos de principio a fin
  Para confirmar que todo el flujo de compra funciona derechito

  # Antecedentes: lo que se hace SIEMPRE antes de cada escenario, pa' no repetir
  Antecedentes:
    Dado que el cliente abre la página de SauceDemo

  @e2e @feliz
  Escenario: Comprar dos productos hasta la confirmación de la orden
    # Paso 1: nos logueamos con el usuario que nos dieron
    Cuando el cliente se autentica con usuario "standard_user" y clave "secret_sauce"
    # Paso 2: metemos dos cositas al carrito
    Y agrega al carrito los productos:
      | Sauce Labs Backpack    |
      | Sauce Labs Bike Light  |
    # Paso 3: vamos a ver el carrito
    Y visualiza el carrito de compras
    # Validamos que sí están los dos productos que metimos
    Entonces el carrito debe contener 2 productos
    # Paso 4: llenamos el formulario de datos de compra
    Cuando completa el formulario de compra con nombre "Juan" apellido "Perez" y codigo postal "090101"
    # Paso 5: finalizamos la compra
    Y finaliza la compra
    # Validación final: tiene que salir el mensaje de gracias, ahí ganamos ñaño
    Entonces debe visualizar el mensaje de confirmación "Thank you for your order!"
