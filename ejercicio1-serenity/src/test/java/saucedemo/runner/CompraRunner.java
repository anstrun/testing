package saucedemo.runner;

// Importaciones del runner: lo que conecta Cucumber con JUnit 5 y Serenity
import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * CompraRunner: este es el "ñaño arrancador". Es la clase que JUnit detecta
 * y usa pa' encender toda la maquinaria de Cucumber + Serenity.
 * No tiene código por dentro: todo se configura con las anotaciones de arriba.
 */
@Suite                                                  // dice: esto es una suite de pruebas
@IncludeEngines("cucumber")                             // usa el motor de Cucumber
@SelectClasspathResource("features/saucedemo")          // dónde están los .feature
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,             // el "glue": dónde están los steps
        value = "saucedemo.stepdefinitions"
)
public class CompraRunner {
    // Vacío a propósito: el runner solo necesita las anotaciones pa' funcionar
}
