package com.synergy6.ms_encomiendas.service;

import com.synergy6.ms_encomiendas.client.AuditoriaClient;
import com.synergy6.ms_encomiendas.dto.EncomiendaRequest;
import com.synergy6.ms_encomiendas.dto.EncomiendaResponse;
import com.synergy6.ms_encomiendas.dto.PageResponse;
import com.synergy6.ms_encomiendas.exception.BusinessException;
import com.synergy6.ms_encomiendas.model.Encomienda;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import com.synergy6.ms_encomiendas.repository.EncomiendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EncomiendaService {

    private final EncomiendaRepository encomiendaRepository;
    private final AuditoriaClient auditoriaClient;

    // ── CRUD principal ────────────────────────────────────────────────────────

    public EncomiendaResponse registrar(EncomiendaRequest request,
                                        String usuarioEmail, String usuarioRol) {
        Encomienda encomienda = new Encomienda();
        encomienda.setRemitente(request.getRemitente().trim());
        encomienda.setDestinatario(request.getDestinatario().trim());
        encomienda.setOrigen(request.getOrigen().trim());
        encomienda.setDestino(request.getDestino().trim());
        encomienda.setPeso(request.getPeso());
        encomienda.setPrecio(request.getPrecio());
        encomienda.setEstado(EstadoEncomienda.REGISTRADA);

        Encomienda saved = encomiendaRepository.save(encomienda);

        auditoriaClient.registrar(
                usuarioEmail, usuarioRol, "CREATE", "ENCOMIENDA", saved.getId(),
                "Registrada encomienda de " + saved.getRemitente() + " a " + saved.getDestinatario());

        return EncomiendaResponse.fromEntity(saved);
    }

    public PageResponse<EncomiendaResponse> listarTodas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(
                encomiendaRepository.findAllByOrderByFechaDesc(pageable),
                EncomiendaResponse::fromEntity);
    }

    public PageResponse<EncomiendaResponse> listarPorEstado(EstadoEncomienda estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(
                encomiendaRepository.findByEstadoOrderByFechaDesc(estado, pageable),
                EncomiendaResponse::fromEntity);
    }

    public List<EncomiendaResponse> buscar(String query) {
        return encomiendaRepository.buscarPorTexto(query)
                .stream()
                .map(EncomiendaResponse::fromEntity)
                .toList();
    }

    public List<EncomiendaResponse> buscarPorRemitente(String nombre) {
        return encomiendaRepository.findByRemitenteContainingIgnoreCaseOrderByFechaDesc(nombre)
                .stream()
                .map(EncomiendaResponse::fromEntity)
                .toList();
    }

    public EncomiendaResponse obtenerPorId(Long id) {
        return EncomiendaResponse.fromEntity(findOrThrow(id));
    }

    public EncomiendaResponse actualizar(Long id, EncomiendaRequest request,
                                         String usuarioEmail, String usuarioRol) {
        Encomienda encomienda = findOrThrow(id);

        encomienda.setRemitente(request.getRemitente().trim());
        encomienda.setDestinatario(request.getDestinatario().trim());
        encomienda.setOrigen(request.getOrigen().trim());
        encomienda.setDestino(request.getDestino().trim());
        encomienda.setPeso(request.getPeso());
        encomienda.setPrecio(request.getPrecio());

        Encomienda saved = encomiendaRepository.save(encomienda);

        auditoriaClient.registrar(
                usuarioEmail, usuarioRol, "UPDATE", "ENCOMIENDA", id,
                "Actualizada encomienda #" + id);

        return EncomiendaResponse.fromEntity(saved);
    }

    public EncomiendaResponse actualizarEstado(Long id, EstadoEncomienda nuevoEstado,
                                               String usuarioEmail, String usuarioRol) {
        Encomienda encomienda = findOrThrow(id);

        if (encomienda.getEstado() == EstadoEncomienda.ENTREGADA
                && nuevoEstado != EstadoEncomienda.ENTREGADA) {
            throw new BusinessException("No se puede modificar una encomienda ya entregada");
        }

        EstadoEncomienda estadoAnterior = encomienda.getEstado();
        encomienda.setEstado(nuevoEstado);
        Encomienda saved = encomiendaRepository.save(encomienda);

        auditoriaClient.registrar(
                usuarioEmail, usuarioRol, "ESTADO_CAMBIO", "ENCOMIENDA", id,
                "Estado cambiado de " + estadoAnterior + " a " + nuevoEstado);

        return EncomiendaResponse.fromEntity(saved);
    }

    public void eliminar(Long id, String usuarioEmail, String usuarioRol) {
        Encomienda encomienda = findOrThrow(id);
        encomiendaRepository.delete(encomienda);

        auditoriaClient.registrar(
                usuarioEmail, usuarioRol, "DELETE", "ENCOMIENDA", id,
                "Eliminada encomienda #" + id + " de " + encomienda.getRemitente());
    }

    // ── Helper privado ────────────────────────────────────────────────────────

    private Encomienda findOrThrow(Long id) {
        return encomiendaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Encomienda no encontrada con id: " + id, HttpStatus.NOT_FOUND));
    }
}
