package com.synergy6.ms_auditoria.controller;

import com.synergy6.ms_auditoria.dto.AuditLogRequest;
import com.synergy6.ms_auditoria.dto.AuditLogResponse;
import com.synergy6.ms_auditoria.dto.PageResponse;
import com.synergy6.ms_auditoria.service.AuditLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    /**
     * POST /api/auditoria
     * Registra una nueva entrada de auditoría.
     * Llamado internamente por otros microservicios.
     */
    @PostMapping
    public ResponseEntity<AuditLogResponse> registrar(@Valid @RequestBody AuditLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    /**
     * GET /api/auditoria?page=0&size=20
     * Lista todos los logs paginados. Solo ADMIN.
     */
    @GetMapping
    public ResponseEntity<PageResponse<AuditLogResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.listarTodos(page, size));
    }

    /**
     * GET /api/auditoria/recurso/{recurso}?page=0&size=20
     * Logs filtrados por tipo de recurso (ENCOMIENDA, VIAJE, PASAJE, USUARIO).
     */
    @GetMapping("/recurso/{recurso}")
    public ResponseEntity<PageResponse<AuditLogResponse>> porRecurso(
            @PathVariable String recurso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.porRecurso(recurso.toUpperCase(), page, size));
    }

    /**
     * GET /api/auditoria/recurso/{recurso}/{id}
     * Historial completo de un registro específico (ej. ENCOMIENDA/5).
     */
    @GetMapping("/recurso/{recurso}/{id}")
    public ResponseEntity<List<AuditLogResponse>> historialRecurso(
            @PathVariable String recurso,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.historialRecurso(recurso.toUpperCase(), id));
    }

    /**
     * GET /api/auditoria/usuario/{email}?page=0&size=20
     * Logs de un usuario específico.
     */
    @GetMapping("/usuario/{email}")
    public ResponseEntity<PageResponse<AuditLogResponse>> porUsuario(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.porUsuario(email, page, size));
    }
}
