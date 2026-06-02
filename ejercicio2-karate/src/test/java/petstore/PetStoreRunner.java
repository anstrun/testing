package petstore;

// Importamos lo necesario pa' arrancar Karate desde JUnit 5
import com.intuit.karate.junit5.Karate;

/**
 * PetStoreRunner: el ñaño que enciende las pruebas de Karate.
 * Karate detecta esta clase y corre los archivos .feature que le indiquemos.
 */
class PetStoreRunner {

    /**
     * Este metodo le dice a Karate que corra TODAS las features
     * que esten en el mismo paquete "petstore" (relativo a esta clase).
     * La anotacion @Karate.Test marca esto como prueba de Karate.
     */
    @Karate.Test
    Karate correrPruebasDeLaPetStore() {
        // relativeTo(getClass()) = busca los .feature al lado de esta clase
        return Karate.run().relativeTo(getClass());
    }
}
