/**
 * karate-config.js
 * Este archivo lo lee Karate ANTES de cada feature. Sirve pa' dejar
 * configuradas las variables globales, como la URL base de la API.
 * Asi no andamos repitiendo la direccion en cada prueba, full vivo.
 */
function fn() {
  // Objeto donde guardamos toda la configuracion
  var config = {
    // baseUrl: la direccion base de la API de la PetStore.
    // Despues en los .feature solo decimos "Given url baseUrl" y listo.
    baseUrl: 'https://petstore.swagger.io/v2'
  };

  // Le damos a Karate un tiempo de espera (en milisegundos) pa' conectar y leer.
  // Si la API se demora mas que esto, falla en vez de quedarse colgado.
  karate.configure('connectTimeout', 10000); // 10 segundos pa' conectar
  karate.configure('readTimeout', 10000);    // 10 segundos pa' leer la respuesta

  // Devolvemos la config pa' que Karate la use en todas las features
  return config;
}
