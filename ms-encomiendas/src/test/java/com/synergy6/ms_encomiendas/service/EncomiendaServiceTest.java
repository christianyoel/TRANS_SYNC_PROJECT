package com.synergy6.ms_encomiendas.service;

import com.synergy6.ms_encomiendas.client.AuditoriaClient;
import com.synergy6.ms_encomiendas.dto.EncomiendaRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaResponse;
import com.synergy6.ms_encomiendas.dto.PageResponse;
import com.synergy6.ms_encomiendas.exception.BusinessException;
import com.synergy6.ms_encomiendas.model.Encomienda;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import com.synergy6.ms_encomiendas.repository.EncomiendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EncomiendaService — pruebas unitarias")
class EncomiendaServiceTest {

    @Mock
    private EncomiendaRepository encomiendaRepository;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private EncomiendaService encomiendaService;

    private Encomienda encomiendaBase;
    private EncomiendaRequest requestBase;

    @BeforeEach
    void setUp() {
        encomiendaBase = new Encomienda();
        encomiendaBase.setId(1L);
        encomiendaBase.setRemitente("Juan Pérez");
        encomiendaBase.setDestinatario("María López");
        encomiendaBase.setOrigen("Juliaca");
        encomiendaBase.setDestino("Cusco");
        encomiendaBase.setPeso(new BigDecimal("5.00"));
        encomiendaBase.setPrecio(new BigDecimal("25.00"));
        encomiendaBase.setEstado(EstadoEncomienda.REGISTRADA);
        encomiendaBase.setFecha(LocalDateTime.now());

        requestBase = new EncomiendaRequest();
        requestBase.setRemitente("Juan Pérez");
        requestBase.setDestinatario("María López");
        requestBase.setOrigen("Juliaca");
        requestBase.setDestino("Cusco");
        requestBase.setPeso(new BigDecimal("5.00"));
        requestBase.setPrecio(new BigDecimal("25.00"));
    }

    // ── registrar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("registrar: debe persistir y devolver EncomiendaResponse")
    void registrar_debeGuardarYDevolverResponse() {
        when(encomiendaRepository.save(any(Encomienda.class))).thenReturn(encomiendaBase);

        EncomiendaResponse response = encomiendaService.registrar(requestBase, "admin@test.com", "ADMIN");

        assertThat(response).isNotNull();
        assertThat(response.getRemitente()).isEqualTo("Juan Pérez");
        assertThat(response.getEstado()).isEqualTo(EstadoEncomienda.REGISTRADA);

        verify(encomiendaRepository).save(any(Encomienda.class));
        verify(auditoriaClient).registrar(
                eq("admin@test.com"), eq("ADMIN"), eq("CREATE"),
                eq("ENCOMIENDA"), eq(1L), anyString());
    }

    @Test
    @DisplayName("registrar: debe trimear espacios en remitente y destinatario")
    void registrar_debeTrimearEspacios() {
        requestBase.setRemitente("  Juan Pérez  ");
        requestBase.setDestinatario("  María López  ");
        when(encomiendaRepository.save(any(Encomienda.class))).thenAnswer(inv -> {
            Encomienda e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        EncomiendaResponse response = encomiendaService.registrar(requestBase, "u@u.com", "ADMIN");

        assertThat(response.getRemitente()).isEqualTo("Juan Pérez");
        assertThat(response.getDestinatario()).isEqualTo("María López");
    }

    // ── listarTodas ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("listarTodas: debe devolver PageResponse con content correcto")
    void listarTodas_debeRetornarPageResponse() {
        var springPage = new PageImpl<>(List.of(encomiendaBase), PageRequest.of(0, 10), 1);
        when(encomiendaRepository.findAllByOrderByFechaDesc(any())).thenReturn(springPage);

        PageResponse<EncomiendaResponse> result = encomiendaService.listarTodas(0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();
    }

    // ── obtenerPorId ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorId: debe devolver la encomienda cuando existe")
    void obtenerPorId_cuandoExiste_debeRetornarResponse() {
        when(encomiendaRepository.findById(1L)).thenReturn(Optional.of(encomiendaBase));

        EncomiendaResponse response = encomiendaService.obtenerPorId(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getRemitente()).isEqualTo("Juan Pérez");
    }

    @Test
    @DisplayName("obtenerPorId: debe lanzar BusinessException cuando no existe")
    void obtenerPorId_cuandoNoExiste_debeLanzarBusinessException() {
        when(encomiendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> encomiendaService.obtenerPorId(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("99");
    }

    // ── actualizarEstado ──────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizarEstado: debe cambiar REGISTRADA → EN_TRANSITO correctamente")
    void actualizarEstado_debeActualizarEstado() {
        when(encomiendaRepository.findById(1L)).thenReturn(Optional.of(encomiendaBase));
        when(encomiendaRepository.save(any())).thenReturn(encomiendaBase);

        encomiendaService.actualizarEstado(1L, EstadoEncomienda.EN_TRANSITO, "u@u.com", "ADMIN");

        verify(encomiendaRepository).save(argThat(e ->
                e.getEstado() == EstadoEncomienda.EN_TRANSITO));
        verify(auditoriaClient).registrar(
                anyString(), anyString(), eq("ESTADO_CAMBIO"),
                eq("ENCOMIENDA"), eq(1L), anyString());
    }

    @Test
    @DisplayName("actualizarEstado: debe lanzar BusinessException si intenta modificar ENTREGADA")
    void actualizarEstado_cuandoEntregada_debeLanzarBusinessException() {
        encomiendaBase.setEstado(EstadoEncomienda.ENTREGADA);
        when(encomiendaRepository.findById(1L)).thenReturn(Optional.of(encomiendaBase));

        assertThatThrownBy(() ->
                encomiendaService.actualizarEstado(1L, EstadoEncomienda.REGISTRADA, "u@u.com", "ADMIN"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("entregada");
    }

    // ── eliminar ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminar: debe llamar a delete y registrar auditoría")
    void eliminar_debeLlamarDeleteYAuditoria() {
        when(encomiendaRepository.findById(1L)).thenReturn(Optional.of(encomiendaBase));
        doNothing().when(encomiendaRepository).delete(encomiendaBase);

        encomiendaService.eliminar(1L, "admin@test.com", "ADMIN");

        verify(encomiendaRepository).delete(encomiendaBase);
        verify(auditoriaClient).registrar(
                eq("admin@test.com"), eq("ADMIN"), eq("DELETE"),
                eq("ENCOMIENDA"), eq(1L), anyString());
    }

    @Test
    @DisplayName("eliminar: debe lanzar BusinessException si la encomienda no existe")
    void eliminar_cuandoNoExiste_debeLanzarBusinessException() {
        when(encomiendaRepository.findById(55L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> encomiendaService.eliminar(55L, "u@u.com", "ADMIN"))
                .isInstanceOf(BusinessException.class);

        verify(encomiendaRepository, never()).delete(any());
    }
}
