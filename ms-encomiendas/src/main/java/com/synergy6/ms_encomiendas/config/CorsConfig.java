package com.synergy6.ms_encomiendas.config;

/**
 * CORS es manejado exclusivamente por el API Gateway (puerto 8080).
 * Este microservicio corre detrás del gateway y no debe agregar sus propios
 * headers Access-Control-Allow-Origin porque duplicarlos rompe las peticiones
 * del navegador (el gateway ya los inyecta).
 *
 * Si alguna vez se necesita acceder directamente al puerto 8084 (sin gateway),
 * descomentar la implementación de WebMvcConfigurer a continuación.
 */
// @Configuration
// public class CorsConfig implements WebMvcConfigurer {
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("http://localhost:4200")
//                 .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                 .allowedHeaders("*")
//                 .allowCredentials(true)
//                 .maxAge(3600);
//     }
// }
public class CorsConfig {
    // Ver comentario arriba
}
