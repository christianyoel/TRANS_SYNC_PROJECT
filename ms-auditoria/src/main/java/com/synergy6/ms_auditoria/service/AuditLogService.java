package com.synergy6.ms_auditoria.service;

import com.synergy6.ms_auditoria.dto.AuditLogRequest;
import com.synergy6.ms_auditoria.dto.AuditLogResponse;
import com.synergy6.ms_auditoria.dto.PageResponse;
import com.synergy6.ms_auditoria.model.AuditLog;
import com.synergy6.ms_auditoria.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repository;

    /**
     * Registra una nueva entrada de auditoría.
     * Llamado por otros microservicios vía HTTP POST /api/auditoria.
     */
    public AuditLogResponse registrar(AuditLogRequest request) {
        AuditLog log = new AuditLog();
        log.setUsuarioEmail(request.getUsuarioEmail());
        log.setUsuarioRol(request.getUsuarioRol());
        log.setAccion(request.getAccion());
        log.setRecurso(request.getRecurso());
        log.setRecursoId(request.getRecursoId());
        log.setDetalle(request.getDetalle());
        return AuditLogResponse.fromEntity(repository.save(log));
    }

    /** Todos los logs paginados, más recientes primero. */
    public PageResponse<AuditLogResponse> listarTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(
                repository.findAll(pageable),
                AuditLogResponse::fromEntity);
    }

    /** Historial completo de un recurso específico (ej. ENCOMIENDA id=5). */
    public List<AuditLogResponse> historialRecurso(String recurso, Long recursoId) {
        return repository.findByRecursoAndRecursoIdOrderByTimestampDesc(recurso, recursoId)
                .stream()
                .map(AuditLogResponse::fromEntity)
                .toList();
    }

    /** Logs filtrados por recurso con paginación. */
    public PageResponse<AuditLogResponse> porRecurso(String recurso, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(
                repository.findByRecursoOrderByTimestampDesc(recurso, pageable),
                AuditLogResponse::fromEntity);
    }

    /** Logs filtrados por usuario con paginación. */
    public PageResponse<AuditLogResponse> porUsuario(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(
                repository.findByUsuarioEmailOrderByTimestampDesc(email, pageable),
                AuditLogResponse::fromEntity);
    }
}
