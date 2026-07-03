package com.synergy6.ms_encomiendas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Prueba de arranque del contexto Spring Boot.
 * Usa H2 en memoria para no depender de PostgreSQL en el entorno de CI/CD.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "auditoria.url=http://localhost:9999"  // URL falsa; AuditoriaClient falla silencioso
})
@DisplayName("Contexto Spring Boot arranca correctamente")
class MsEncomiendasApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto arranca sin excepción, el test pasa
    }
}
