package com.synergy6.ms_auditoria.dto;

import com.synergy6.ms_auditoria.model.Accion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO que los microservicios envían a ms-auditoria para registrar una operación.
 * Los campos usuarioEmail y usuarioRol los extrae el API Gateway de los headers
 * X-User-Email y X-User-Rol que inyecta tras validar el JWT.
 */
@Data
public class AuditLogRequest {

    @NotBlank(message = "El email del usuario es obligatorio")
    private String usuarioEmail;

    @NotBlank(message = "El rol del usuario es obligatorio")
    private String usuarioRol;

    @NotNull(message = "La acción es obligatoria")
    private Accion accion;

    @NotBlank(message = "El recurso es obligatorio")
    private String recurso;

    private Long recursoId;

    private String detalle;
}
