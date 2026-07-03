package com.synergy6.ms_encomiendas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy6.ms_encomiendas.dto.EncomiendaEstadoRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaResponse;
import com.synergy6.ms_encomiendas.dto.PageResponse;
import com.synergy6.ms_encomiendas.exception.BusinessException;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import com.synergy6.ms_encomiendas.service.EncomiendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EncomiendaController.class)
@DisplayName("EncomiendaController — pruebas de capa web")
class EncomiendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EncomiendaService encomiendaService;

    private EncomiendaResponse responseBase;

    @BeforeEach
    void setUp() {
        responseBase = EncomiendaResponse.builder()
                .id(1L)
                .remitente("Juan Pérez")
                .destinatario("María López")
                .origen("Juliaca")
                .destino("Cusco")
                .peso(new BigDecimal("5.00"))
                .precio(new BigDecimal("25.00"))
                .estado(EstadoEncomienda.REGISTRADA)
                .fecha(LocalDateTime.now())
                .build();
    }

    // ── GET /api/encomiendas ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/encomiendas debe devolver 200 con PageResponse")
    void listarTodas_debeRetornar200() throws Exception {
        PageResponse<EncomiendaResponse> page = PageResponse.<EncomiendaResponse>builder()
                .content(List.of(responseBase))
                .page(0).size(10).totalElements(1).totalPages(1).last(true)
                .build();

        when(encomiendaService.listarTodas(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/encomiendas")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].remitente").value("Juan Pérez"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    // ── POST /api/encomiendas ─────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/encomiendas debe devolver 201 con la encomienda creada")
    void registrar_debeRetornar201() throws Exception {
        EncomiendaRequest req = new EncomiendaRequest();
        req.setRemitente("Juan Pérez");
        req.setDestinatario("María López");
        req.setOrigen("Juliaca");
        req.setDestino("Cusco");
        req.setPeso(new BigDecimal("5.00"));
        req.setPrecio(new BigDecimal("25.00"));

        when(encomiendaService.registrar(any(), anyString(), anyString()))
                .thenReturn(responseBase);

        mockMvc.perform(post("/api/encomiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("REGISTRADA"));
    }

    @Test
    @DisplayName("POST /api/encomiendas con datos inválidos debe devolver 400")
    void registrar_conDatosInvalidos_debeRetornar400() throws Exception {
        EncomiendaRequest req = new EncomiendaRequest(); // campos vacíos

        mockMvc.perform(post("/api/encomiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/encomiendas/{id} ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/encomiendas/1 debe devolver 200 con la encomienda")
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(encomiendaService.obtenerPorId(1L)).thenReturn(responseBase);

        mockMvc.perform(get("/api/encomiendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.remitente").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /api/encomiendas/99 debe devolver 404 cuando no existe")
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(encomiendaService.obtenerPorId(99L))
                .thenThrow(new BusinessException("Encomienda no encontrada con id: 99", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/encomiendas/99"))
                .andExpect(status().isNotFound());
    }

    // ── PATCH /api/encomiendas/{id}/estado ───────────────────────────────────

    @Test
    @DisplayName("PATCH /api/encomiendas/1/estado debe devolver 200 con estado actualizado")
    void actualizarEstado_debeRetornar200() throws Exception {
        EncomiendaResponse enTransito = EncomiendaResponse.builder()
                .id(1L).remitente("Juan Pérez").destinatario("María López")
                .origen("Juliaca").destino("Cusco")
                .peso(new BigDecimal("5.00")).precio(new BigDecimal("25.00"))
                .estado(EstadoEncomienda.EN_TRANSITO).fecha(LocalDateTime.now())
                .build();

        EncomiendaEstadoRequest estadoReq = new EncomiendaEstadoRequest();
        estadoReq.setEstado(EstadoEncomienda.EN_TRANSITO);

        when(encomiendaService.actualizarEstado(eq(1L), eq(EstadoEncomienda.EN_TRANSITO),
                anyString(), anyString()))
                .thenReturn(enTransito);

        mockMvc.perform(patch("/api/encomiendas/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_TRANSITO"));
    }

    // ── GET /api/encomiendas/buscar ───────────────────────────────────────────

    @Test
    @DisplayName("GET /api/encomiendas/buscar?q=juan debe devolver lista con resultados")
    void buscar_debeRetornarResultados() throws Exception {
        when(encomiendaService.buscar("juan")).thenReturn(List.of(responseBase));

        mockMvc.perform(get("/api/encomiendas/buscar").param("q", "juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].remitente").value("Juan Pérez"));
    }

    // ── DELETE /api/encomiendas/{id} ──────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/encomiendas/1 debe devolver 204")
    void eliminar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/encomiendas/1"))
                .andExpect(status().isNoContent());
    }
}
