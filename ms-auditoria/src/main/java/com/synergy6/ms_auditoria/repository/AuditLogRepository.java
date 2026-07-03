package com.synergy6.ms_auditoria.repository;

import com.synergy6.ms_auditoria.model.AuditLog;
import com.synergy6.ms_auditoria.model.Accion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /** Logs de un usuario específico, más reciente primero. */
    Page<AuditLog> findByUsuarioEmailOrderByTimestampDesc(String usuarioEmail, Pageable pageable);

    /** Logs de un recurso específico (ej. todos los cambios de ENCOMIENDA). */
    Page<AuditLog> findByRecursoOrderByTimestampDesc(String recurso, Pageable pageable);

    /** Logs de un recurso y ID específico (ej. historial de ENCOMIENDA con id=5). */
    List<AuditLog> findByRecursoAndRecursoIdOrderByTimestampDesc(String recurso, Long recursoId);

    /** Logs entre dos fechas. */
    Page<AuditLog> findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    /** Logs por tipo de acción. */
    Page<AuditLog> findByAccionOrderByTimestampDesc(Accion accion, Pageable pageable);
}
