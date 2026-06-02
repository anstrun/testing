package saucedemo.userinterface;

import net.serenitybdd.screenplay.targets.Target;

/**
 * ProductsPage: la libretita con las direcciones de la pantalla de productos
 * (la del inventario, donde salen todas las cositas a la venta).
 */
public class ProductsPage {

    // El iconito del carrito, arriba a la derecha. Por su clase "shopping_cart_link"
    public static final Target ICONO_CARRITO = Target
            .the("icono del carrito")
            .locatedBy(".shopping_cart_link");

    /**
     * Botón dinámico de "Add to cart" según el nombre del producto.
     * En SauceDemo el id del botón se arma con el nombre en minúsculas y guiones,
     * por ejemplo: "add-to-cart-sauce-labs-backpack".
     * El {0} es un huequito que después rellenamos con el nombre ya formateado.
     */
    public static Target botonAgregarProducto(String idProducto) {
        return Target
                .the("boton agregar " + idProducto)
                .locatedBy("#add-to-cart-" + idProducto);
    }
}
