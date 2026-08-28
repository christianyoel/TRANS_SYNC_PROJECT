package com.synergy6.ms_encomiendas.controller;

import com.synergy6.ms_encomiendas.dto.EncomiendaEstadoRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaResponse;
import com.synergy6.ms_encomiendas.dto.PageResponse;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import com.synergy6.ms_encomiendas.service.EncomiendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios del EncomiendaController usando Mockito puro.
 * Verifica que el controller delega correctamente al service
 * y devuelve los códigos HTTP esperados.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EncomiendaController — pruebas unitarias con Mockito")
class EncomiendaControllerTest {

    @Mock
    private EncomiendaService encomiendaService;

    @InjectMocks
    private EncomiendaController encomiendaController;

    private EncomiendaResponse responseBase;
    private EncomiendaRequest requestBase;

    @BeforeEach
    void setUp() {
        responseBase = EncomiendaResponse.builder()
                .id(1L)
                .remitente("Ana Torres")
                .destinatario("Luis Quispe")
                .origen("Juliaca").destino("Cusco")
                .peso(new BigDecimal("3.50"))
                .precio(new BigDecimal("20.00"))
                .estado(EstadoEncomienda.REGISTRADA)
                .fecha(LocalDateTime.now())
                .build();

        requestBase = new EncomiendaRequest();
        requestBase.setRemitente("Ana Torres");
        requestBase.setDestinatario("Luis Quispe");
        requestBase.setOrigen("Juliaca");
        requestBase.setDestino("Cusco");
        requestBase.setPeso(new BigDecimal("3.50"));
        requestBase.setPrecio(new BigDecimal("20.00"));
    }

    // ── POST /api/encomiendas ─────────────────────────────────────────────

    @Test
    @DisplayName("registrar() debe devolver 201 CREATED con el body correcto")
    void registrar_debeRetornar201() {
        when(encomiendaService.registrar(any(), anyString(), anyString()))
                .thenReturn(responseBase);

        ResponseEntity<EncomiendaResponse> resp =
                encomiendaController.registrar(requestBase, "admin@test.com", "ADMIN");

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getRemitente()).isEqualTo("Ana Torres");
        assertThat(resp.getBody().getEstado()).isEqualTo(EstadoEncomienda.REGISTRADA);
    }

    // ── GET /api/encomiendas ──────────────────────────────────────────────

    @Test
    @DisplayName("listarTodas() debe devolver 200 con PageResponse")
    void listarTodas_debeRetornar200ConPageResponse() {
        PageResponse<EncomiendaResponse> page = PageResponse.<EncomiendaResponse>builder()
                .content(List.of(responseBase))
                .page(0).size(10).totalElements(1).totalPages(1).last(true)
                .build();

        when(encomiendaService.listarTodas(0, 10)).thenReturn(page);

        ResponseEntity<PageResponse<EncomiendaResponse>> resp =
                encomiendaController.listarTodas(0, 10);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().getContent()).hasSize(1);
        assertThat(resp.getBody().getTotalElements()).isEqualTo(1);
    }

    // ── GET /api/encomiendas/{id} ─────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorId() debe devolver 200 cuando existe")
    void obtenerPorId_cuandoExiste_debeRetornar200() {
        when(encomiendaService.obtenerPorId(1L)).thenReturn(responseBase);

        ResponseEntity<EncomiendaResponse> resp =
                encomiendaController.obtenerPorId(1L);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().getId()).isEqualTo(1L);
    }

    // ── PATCH /api/encomiendas/{id}/estado ───────────────────────────────

    @Test
    @DisplayName("actualizarEstado() debe devolver 200 con el nuevo estado")
    void actualizarEstado_debeRetornar200() {
        EncomiendaResponse enTransito = EncomiendaResponse.builder()
                .id(1L).remitente("Ana Torres").destinatario("Luis Quispe")
                .origen("Juliaca").destino("Cusco")
                .peso(new BigDecimal("3.50")).precio(new BigDecimal("20.00"))
                .estado(EstadoEncomienda.EN_TRANSITO)
                .fecha(LocalDateTime.now())
                .build();

        when(encomiendaService.actualizarEstado(eq(1L),
                eq(EstadoEncomienda.EN_TRANSITO), anyString(), anyString()))
                .thenReturn(enTransito);

        EncomiendaEstadoRequest req = new EncomiendaEstadoRequest();
        req.setEstado(EstadoEncomienda.EN_TRANSITO);

        ResponseEntity<EncomiendaResponse> resp =
                encomiendaController.actualizarEstado(1L, req, "admin@test.com", "ADMIN");

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().getEstado()).isEqualTo(EstadoEncomienda.EN_TRANSITO);
    }

    // ── GET /api/encomiendas/buscar ───────────────────────────────────────

    @Test
    @DisplayName("buscar() debe devolver 200 con lista de resultados")
    void buscar_debeRetornar200ConResultados() {
        when(encomiendaService.buscar("Ana")).thenReturn(List.of(responseBase));

        ResponseEntity<List<EncomiendaResponse>> resp =
                encomiendaController.buscar("Ana");

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).hasSize(1);
        assertThat(resp.getBody().get(0).getRemitente()).isEqualTo("Ana Torres");
    }

    // ── DELETE /api/encomiendas/{id} ──────────────────────────────────────

    @Test
    @DisplayName("eliminar() debe devolver 204 NO_CONTENT")
    void eliminar_debeRetornar204() {
        ResponseEntity<Void> resp =
                encomiendaController.eliminar(1L, "admin@test.com", "ADMIN");

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
