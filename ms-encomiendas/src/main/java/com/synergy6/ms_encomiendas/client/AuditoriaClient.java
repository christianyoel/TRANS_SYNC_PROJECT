package com.synergy6.ms_encomiendas.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Cliente HTTP que notifica al ms-auditoria tras cada operación CRUD.
 * Las llamadas son best-effort: si ms-auditoria no está disponible, se
 * loguea el error pero la operación principal no se interrumpe.
 */
@Component
public class AuditoriaClient {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaClient.class);

    private final RestClient restClient;

    public AuditoriaClient(
            @Value("${auditoria.url:http://localhost:8085}") String auditoriaUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(auditoriaUrl)
                .build();
    }

    /**
     * Registra una operación en el servicio de auditoría de forma síncrona.
     * El fallo no propaga excepción — solo se loguea.
     */
    public void registrar(String email, String rol, String accion,
                          String recurso, Long recursoId, String detalle) {
        try {
            restClient.post()
                    .uri("/api/auditoria")
                    .body(Map.of(
                            "usuarioEmail", email != null ? email : "sistema",
                            "usuarioRol",  rol  != null ? rol  : "SISTEMA",
                            "accion",      accion,
                            "recurso",     recurso,
                            "recursoId",   recursoId != null ? recursoId : 0,
                            "detalle",     detalle != null ? detalle : ""
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("No se pudo registrar auditoría [{} {} {}]: {}", accion, recurso, recursoId, e.getMessage());
        }
    }
}
