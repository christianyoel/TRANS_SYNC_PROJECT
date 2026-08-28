package com.synergy6.ms_encomiendas.controller;

import com.synergy6.ms_encomiendas.dto.EncomiendaEstadoRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaResponse;
import com.synergy6.ms_encomiendas.dto.PageResponse;
import com.synergy6.ms_encomiendas.dto.ResumenEncomiendas;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import com.synergy6.ms_encomiendas.service.EncomiendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encomiendas")
@RequiredArgsConstructor
public class EncomiendaController {

    private final EncomiendaService encomiendaService;

    // ── Lectura ───────────────────────────────────────────────────────────────

    /**
     * GET /api/encomiendas/resumen
     * Conteos por estado para el Dashboard. Solo ADMIN (requiere token).
     */
    @GetMapping("/resumen")
    public ResponseEntity<ResumenEncomiendas> resumen() {
        return ResponseEntity.ok(encomiendaService.resumen());
    }

    /**
     * GET /api/encomiendas?page=0&size=10
     * Lista paginada de todas las encomiendas, ordenadas por fecha desc.
     */
    @GetMapping
    public ResponseEntity<PageResponse<EncomiendaResponse>> listarTodas(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(encomiendaService.listarTodas(page, size));
    }

    /**
     * GET /api/encomiendas/filtrar?estado=REGISTRADA&page=0&size=10
     * Lista paginada filtrada por estado.
     */
    @GetMapping("/filtrar")
    public ResponseEntity<PageResponse<EncomiendaResponse>> listarPorEstado(
            @RequestParam EstadoEncomienda estado,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(encomiendaService.listarPorEstado(estado, page, size));
    }

    /**
     * GET /api/encomiendas/buscar?q=juan
     * Búsqueda de texto libre en remitente y destinatario.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<EncomiendaResponse>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(encomiendaService.buscar(q));
    }

    /**
     * GET /api/encomiendas/seguimiento?nombre=juan
     * Endpoint público: busca por nombre de remitente o destinatario sin autenticación.
     */
    @GetMapping("/seguimiento")
    public ResponseEntity<List<EncomiendaResponse>> seguimiento(@RequestParam String nombre) {
        return ResponseEntity.ok(encomiendaService.buscarPorRemitente(nombre));
    }

    /**
     * GET /api/encomiendas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EncomiendaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(encomiendaService.obtenerPorId(id));
    }

    // ── Escritura ─────────────────────────────────────────────────────────────

    /**
     * POST /api/encomiendas
     * Registra una nueva encomienda. Accesible por ADMIN y COUNTER.
     */
    @PostMapping
    public ResponseEntity<EncomiendaResponse> registrar(
            @Valid @RequestBody EncomiendaRequest request,
            @RequestHeader(value = "X-User-Email", defaultValue = "sistema") String email,
            @RequestHeader(value = "X-User-Rol",   defaultValue = "SISTEMA") String rol) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(encomiendaService.registrar(request, email, rol));
    }

    /**
     * PUT /api/encomiendas/{id}
     * Actualiza datos de una encomienda. Solo ADMIN.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EncomiendaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EncomiendaRequest request,
            @RequestHeader(value = "X-User-Email", defaultValue = "sistema") String email,
            @RequestHeader(value = "X-User-Rol",   defaultValue = "SISTEMA") String rol) {
        return ResponseEntity.ok(encomiendaService.actualizar(id, request, email, rol));
    }

    /**
     * PATCH /api/encomiendas/{id}/estado
     * Cambia el estado de una encomienda. Solo ADMIN.
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EncomiendaResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EncomiendaEstadoRequest request,
            @RequestHeader(value = "X-User-Email", defaultValue = "sistema") String email,
            @RequestHeader(value = "X-User-Rol",   defaultValue = "SISTEMA") String rol) {
        return ResponseEntity.ok(
                encomiendaService.actualizarEstado(id, request.getEstado(), email, rol));
    }

    /**
     * DELETE /api/encomiendas/{id}
     * Elimina una encomienda. Solo ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Email", defaultValue = "sistema") String email,
            @RequestHeader(value = "X-User-Rol",   defaultValue = "SISTEMA") String rol) {
        encomiendaService.eliminar(id, email, rol);
        return ResponseEntity.noContent().build();
    }
}
