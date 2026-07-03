package com.synergy6.ms_auditoria.dto;

import com.synergy6.ms_auditoria.model.AuditLog;
import com.synergy6.ms_auditoria.model.Accion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponse {

    private Long id;
    private String usuarioEmail;
    private String usuarioRol;
    private Accion accion;
    private String recurso;
    private Long recursoId;
    private String detalle;
    private LocalDateTime timestamp;

    public static AuditLogResponse fromEntity(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .usuarioEmail(log.getUsuarioEmail())
                .usuarioRol(log.getUsuarioRol())
                .accion(log.getAccion())
                .recurso(log.getRecurso())
                .recursoId(log.getRecursoId())
                .detalle(log.getDetalle())
                .timestamp(log.getTimestamp())
                .build();
    }
}
