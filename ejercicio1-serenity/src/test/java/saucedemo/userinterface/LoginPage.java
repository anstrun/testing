package saucedemo.userinterface;

// Importamos la clase Target de Serenity, que sirve pa' apuntar a elementos de la página
import net.serenitybdd.screenplay.targets.Target;

/**
 * LoginPage: aquí guardamos las "direcciones" de los elementos de la pantalla de login.
 * Es como tener anotado en una libretita dónde queda cada cosa de esa pantalla, ñaño.
 */
public class LoginPage {

    // Cajita donde se escribe el usuario. La buscamos por su id "user-name"
    public static final Target CAMPO_USUARIO = Target
            .the("campo de usuario")
            .locatedBy("#user-name");

    // Cajita donde va la clave. La ubicamos por su id "password"
    public static final Target CAMPO_CLAVE = Target
            .the("campo de clave")
            .locatedBy("#password");

    // El botón de "Login" pa' entrar. Lo agarramos por su id "login-button"
    public static final Target BOTON_INGRESAR = Target
            .the("boton de ingresar")
            .locatedBy("#login-button");
}
