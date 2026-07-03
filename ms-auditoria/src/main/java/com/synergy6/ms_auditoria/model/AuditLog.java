package com.synergy6.ms_auditoria.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un registro de auditoría.
 * Cada operación relevante del sistema genera un AuditLog con:
 * - quién la ejecutó (usuarioEmail, usuarioRol)
 * - qué hizo (accion: CREATE, UPDATE, DELETE, ESTADO_CAMBIO)
 * - sobre qué recurso (recurso: ENCOMIENDA, VIAJE, PASAJE, USUARIO)
 * - detalles adicionales en JSON libre (detalle)
 */
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email del usuario que realizó la operación (viene del header X-User-Email). */
    @Column(nullable = false, length = 120)
    private String usuarioEmail;

    /** Rol del usuario (ADMIN, COUNTER, CONDUCTOR). */
    @Column(nullable = false, length = 20)
    private String usuarioRol;

    /** Tipo de acción: CREATE, UPDATE, DELETE, ESTADO_CAMBIO, LOGIN. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Accion accion;

    /** Nombre del recurso afectado: ENCOMIENDA, VIAJE, PASAJE, USUARIO. */
    @Column(nullable = false, length = 50)
    private String recurso;

    /** ID del recurso afectado (puede ser null para operaciones de lista). */
    @Column
    private Long recursoId;

    /** Descripción libre en texto de la operación realizada. */
    @Column(columnDefinition = "TEXT")
    private String detalle;

    /** Timestamp automático al persistir. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
