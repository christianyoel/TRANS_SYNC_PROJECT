package com.synergy6.ms_viajes.service;

import com.synergy6.ms_viajes.dto.ViajeRequestDto;
import com.synergy6.ms_viajes.dto.ViajeResponseDto;
import com.synergy6.ms_viajes.exception.BusinessException;
import com.synergy6.ms_viajes.model.Viaje;
import com.synergy6.ms_viajes.model.ViajeEstado;
import com.synergy6.ms_viajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeResponseDto crear(ViajeRequestDto dto) {
        if (viajeRepository.existsByNumeroViaje(dto.getNumeroViaje())) {
            throw new BusinessException("Ya existe un viaje con el número " + dto.getNumeroViaje());
        }

        Viaje viaje = new Viaje();
        viaje.setNumeroViaje(dto.getNumeroViaje());
        viaje.setOrigen(dto.getOrigen());
        viaje.setDestino(dto.getDestino());
        viaje.setFechaSalida(dto.getFechaSalida());
        viaje.setPrecio(dto.getPrecio());
        viaje.setCapacidad(dto.getCapacidad() != null ? dto.getCapacidad() : 7);
        viaje.setEstado(ViajeEstado.ACTIVO);

        return ViajeResponseDto.fromEntity(viajeRepository.save(viaje));
    }

    public List<ViajeResponseDto> listarActivos() {
        return viajeRepository.findByEstado(ViajeEstado.ACTIVO).stream()
                .map(ViajeResponseDto::fromEntity)
                .toList();
    }

    public List<ViajeResponseDto> listarTodos() {
        return viajeRepository.findAll().stream()
                .map(ViajeResponseDto::fromEntity)
                .toList();
    }

    public ViajeResponseDto obtenerPorNumeroViaje(String numeroViaje) {
        return ViajeResponseDto.fromEntity(buscarEntidad(numeroViaje));
    }

    public ViajeResponseDto cambiarEstado(String numeroViaje, ViajeEstado nuevoEstado) {
        Viaje viaje = buscarEntidad(numeroViaje);
        viaje.setEstado(nuevoEstado);
        return ViajeResponseDto.fromEntity(viajeRepository.save(viaje));
    }

    public void marcarCompleto(String numeroViaje) {
        Viaje viaje = buscarEntidad(numeroViaje);
        viaje.setEstado(ViajeEstado.COMPLETO);
        viajeRepository.save(viaje);
    }

    public Viaje buscarEntidad(String numeroViaje) {
        return viajeRepository.findByNumeroViaje(numeroViaje)
                .orElseThrow(() -> new BusinessException("Viaje no encontrado: " + numeroViaje, HttpStatus.NOT_FOUND));
    }

    public void validarViajeVendible(String numeroViaje) {
        Viaje viaje = buscarEntidad(numeroViaje);
        if (viaje.getEstado() != ViajeEstado.ACTIVO) {
            throw new BusinessException("El viaje " + numeroViaje + " no está disponible para venta");
        }
    }
}
