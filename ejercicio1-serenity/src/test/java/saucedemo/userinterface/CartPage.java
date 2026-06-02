package saucedemo.userinterface;

import net.serenitybdd.screenplay.targets.Target;

/**
 * CartPage: las direcciones de la pantalla del carrito (donde se ve lo que metimos).
 */
public class CartPage {

    // Cada producto en el carrito sale dentro de un bloque con clase "cart_item".
    // Esto sirve pa' después contar cuántos hay.
    public static final Target ITEMS_DEL_CARRITO = Target
            .the("items del carrito")
            .locatedBy(".cart_item");

    // El botón "Checkout" pa' pasar a llenar los datos de compra
    public static final Target BOTON_CHECKOUT = Target
            .the("boton checkout")
            .locatedBy("#checkout");
}
