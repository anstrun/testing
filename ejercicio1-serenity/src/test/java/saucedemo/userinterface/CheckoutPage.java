package saucedemo.userinterface;

import net.serenitybdd.screenplay.targets.Target;

/**
 * CheckoutPage: las direcciones de las pantallas de checkout:
 * la del formulario, la del resumen y la de "gracias por tu orden".
 */
public class CheckoutPage {

    // Cajita pa' el primer nombre. Por su id "first-name"
    public static final Target CAMPO_NOMBRE = Target
            .the("campo de nombre")
            .locatedBy("#first-name");

    // Cajita pa' el apellido. Por su id "last-name"
    public static final Target CAMPO_APELLIDO = Target
            .the("campo de apellido")
            .locatedBy("#last-name");

    // Cajita pa' el código postal. Por su id "postal-code"
    public static final Target CAMPO_CODIGO_POSTAL = Target
            .the("campo de codigo postal")
            .locatedBy("#postal-code");

    // Botón "Continue" pa' pasar al resumen de la compra
    public static final Target BOTON_CONTINUAR = Target
            .the("boton continuar")
            .locatedBy("#continue");

    // Botón "Finish" pa' rematar la compra
    public static final Target BOTON_FINALIZAR = Target
            .the("boton finalizar")
            .locatedBy("#finish");

    // El mensajote de "Thank you for your order!" que sale al final si todo salió bien
    public static final Target MENSAJE_CONFIRMACION = Target
            .the("mensaje de confirmacion")
            .locatedBy(".complete-header");
}
