package saucedemo.stepdefinitions;

// --- Importaciones: todas las herramientas que vamos a usar en este archivo ---
import io.cucumber.datatable.DataTable;            // pa' leer las tablitas del .feature
import io.cucumber.java.Before;                    // anotación que corre antes de cada escenario
import io.cucumber.java.es.Cuando;                 // los pasos "Cuando" en español
import io.cucumber.java.es.Dado;                   // los pasos "Dado" en español
import io.cucumber.java.es.Entonces;               // los pasos "Entonces" en español
import io.cucumber.java.es.Y;                      // los pasos "Y" en español
import net.serenitybdd.screenplay.Actor;           // el "actor", o sea el cliente que hace las cosas
import net.serenitybdd.screenplay.abilities.BrowseTheWeb; // habilidad de navegar la web
import net.serenitybdd.screenplay.actions.Click;   // acción de dar clic
import net.serenitybdd.screenplay.actions.Enter;   // acción de escribir texto
import net.serenitybdd.screenplay.actions.Open;    // acción de abrir una URL
import net.serenitybdd.screenplay.questions.Text;  // pa' leer textos de la página
import net.serenitybdd.screenplay.targets.Target;  // los apuntadores a elementos
import org.openqa.selenium.WebDriver;              // el navegador de Selenium

import java.util.List;                             // listas, pa' guardar varios productos

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat; // pa' las validaciones
import static org.hamcrest.Matchers.containsString;             // valida que un texto contenga otro
import static org.hamcrest.Matchers.equalTo;                    // valida que dos cosas sean iguales

/**
 * Aquí cada paso escrito en el .feature se convierte en acciones reales del navegador.
 * Es el traductor entre el español del Gherkin y lo que Serenity de verdad hace.
 */
public class CompraStepDefinitions {

    // El navegador que vamos a manejar (Selenium nos lo inyecta solito)
    private WebDriver hisBrowser;

    // Nuestro actor, le ponemos "Cliente" porque es el que compra
    private Actor cliente = Actor.named("Cliente");

    /**
     * @Before: esto corre ANTES de cada escenario. Le damos al cliente
     * la habilidad de navegar la web usando el navegador inyectado.
     */
    @Before
    public void prepararActor() {
        cliente.can(BrowseTheWeb.with(hisBrowser));
    }

    // ---------------------------------------------------------------
    // PASO: "Dado que el cliente abre la página de SauceDemo"
    // ---------------------------------------------------------------
    @Dado("que el cliente abre la página de SauceDemo")
    public void elClienteAbreLaPagina() {
        // El cliente abre la URL de SauceDemo. Open.url abre esa dirección.
        cliente.attemptsTo(
                Open.url("https://www.saucedemo.com/")
        );
    }

    // ---------------------------------------------------------------
    // PASO: autenticarse con usuario y clave
    // ---------------------------------------------------------------
    @Cuando("el cliente se autentica con usuario {string} y clave {string}")
    public void elClienteSeAutentica(String usuario, String clave) {
        // El cliente escribe el usuario, escribe la clave y da clic en ingresar
        cliente.attemptsTo(
                Enter.theValue(usuario).into(saucedemo.userinterface.LoginPage.CAMPO_USUARIO),
                Enter.theValue(clave).into(saucedemo.userinterface.LoginPage.CAMPO_CLAVE),
                Click.on(saucedemo.userinterface.LoginPage.BOTON_INGRESAR)
        );
    }

    // ---------------------------------------------------------------
    // PASO: agregar productos al carrito (recibe una tabla del .feature)
    // ---------------------------------------------------------------
    @Y("agrega al carrito los productos:")
    public void agregaAlCarritoLosProductos(DataTable tabla) {
        // Convertimos la tablita en una lista simple de nombres de producto
        List<String> productos = tabla.asList();

        // Recorremos cada producto de la lista, uno por uno
        for (String nombreProducto : productos) {
            // SauceDemo arma el id en minúsculas y con guiones en vez de espacios.
            // Ejemplo: "Sauce Labs Backpack" -> "sauce-labs-backpack"
            String idProducto = nombreProducto.trim()
                    .toLowerCase()
                    .replace(" ", "-");

            // Damos clic en el botón "Add to cart" de ese producto
            cliente.attemptsTo(
                    Click.on(saucedemo.userinterface.ProductsPage.botonAgregarProducto(idProducto))
            );
        }
    }

    // ---------------------------------------------------------------
    // PASO: visualizar el carrito
    // ---------------------------------------------------------------
    @Y("visualiza el carrito de compras")
    public void visualizaElCarrito() {
        // Damos clic en el iconito del carrito pa' ir a verlo
        cliente.attemptsTo(
                Click.on(saucedemo.userinterface.ProductsPage.ICONO_CARRITO)
        );
    }

    // ---------------------------------------------------------------
    // VALIDACIÓN: el carrito debe contener N productos
    // ---------------------------------------------------------------
    @Entonces("el carrito debe contener {int} productos")
    public void elCarritoDebeContener(int cantidadEsperada) {
        // Contamos cuántos bloques ".cart_item" hay en pantalla
        Target itemsCarrito = saucedemo.userinterface.CartPage.ITEMS_DEL_CARRITO;
        int cantidadReal = itemsCarrito.resolveAllFor(cliente).size();

        // Validamos que la cantidad real sea igual a la esperada
        cliente.should(
                seeThat("la cantidad de productos en el carrito",
                        actor -> cantidadReal, equalTo(cantidadEsperada))
        );
    }

    // ---------------------------------------------------------------
    // PASO: completar el formulario de compra
    // ---------------------------------------------------------------
    @Cuando("completa el formulario de compra con nombre {string} apellido {string} y codigo postal {string}")
    public void completaElFormulario(String nombre, String apellido, String codigoPostal) {
        // Primero damos clic en checkout pa' ir al formulario,
        // luego llenamos los tres campos y damos clic en continuar
        cliente.attemptsTo(
                Click.on(saucedemo.userinterface.CartPage.BOTON_CHECKOUT),
                Enter.theValue(nombre).into(saucedemo.userinterface.CheckoutPage.CAMPO_NOMBRE),
                Enter.theValue(apellido).into(saucedemo.userinterface.CheckoutPage.CAMPO_APELLIDO),
                Enter.theValue(codigoPostal).into(saucedemo.userinterface.CheckoutPage.CAMPO_CODIGO_POSTAL),
                Click.on(saucedemo.userinterface.CheckoutPage.BOTON_CONTINUAR)
        );
    }

    // ---------------------------------------------------------------
    // PASO: finalizar la compra
    // ---------------------------------------------------------------
    @Y("finaliza la compra")
    public void finalizaLaCompra() {
        // Damos clic en el botón "Finish" pa' rematar la orden
        cliente.attemptsTo(
                Click.on(saucedemo.userinterface.CheckoutPage.BOTON_FINALIZAR)
        );
    }

    // ---------------------------------------------------------------
    // VALIDACIÓN FINAL: debe salir el mensaje de gracias
    // ---------------------------------------------------------------
    @Entonces("debe visualizar el mensaje de confirmación {string}")
    public void debeVisualizarElMensaje(String mensajeEsperado) {
        // Leemos el texto del encabezado de confirmación y validamos
        // que contenga el mensaje esperado (sin importar mayúsculas exactas)
        cliente.should(
                seeThat("el mensaje de confirmacion",
                        Text.of(saucedemo.userinterface.CheckoutPage.MENSAJE_CONFIRMACION),
                        containsString(mensajeEsperado))
        );
    }
}
