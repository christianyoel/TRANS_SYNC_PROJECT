package com.synergy6.ms_pasajes.service;

import com.synergy6.ms_pasajes.client.ViajeClient;
import com.synergy6.ms_pasajes.dto.*;
import com.synergy6.ms_pasajes.exception.BusinessException;
import com.synergy6.ms_pasajes.model.Pasaje;
import com.synergy6.ms_pasajes.repository.PasajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PasajeService {

    private final PasajeRepository pasajeRepository;
    private final ViajeClient viajeClient;

    public PasajeResponseDto venderPasaje(VenderPasajeRequestDto dto) {
        ViajeExternoDto viaje = viajeClient.obtenerViaje(dto.getNumeroViaje());

        if (!"ACTIVO".equals(viaje.getEstado())) {
            throw new BusinessException("El viaje " + dto.getNumeroViaje() + " no está disponible para venta");
        }

        if (dto.getAsiento() < 1 || dto.getAsiento() > viaje.getCapacidad()) {
            throw new BusinessException("La capacidad del viaje es de " + viaje.getCapacidad() + " asientos (1-" + viaje.getCapacidad() + ")");
        }

        if (pasajeRepository.existsByNumeroViajeAndAsiento(dto.getNumeroViaje(), dto.getAsiento())) {
            throw new BusinessException("El asiento " + dto.getAsiento() + " ya está ocupado en el viaje " + dto.getNumeroViaje());
        }

        Pasaje pasaje = new Pasaje();
        pasaje.setNumeroViaje(viaje.getNumeroViaje());
        pasaje.setOrigen(viaje.getOrigen());
        pasaje.setDestino(viaje.getDestino());
        pasaje.setFechaSalida(viaje.getFechaSalida());
        pasaje.setAsiento(dto.getAsiento());
        pasaje.setDocumentoPasajero(dto.getDocumentoPasajero());
        pasaje.setNombrePasajero(dto.getNombrePasajero());
        pasaje.setPrecio(viaje.getPrecio());

        Pasaje guardado = pasajeRepository.save(pasaje);

        long vendidos = pasajeRepository.countByNumeroViaje(dto.getNumeroViaje());
        if (vendidos >= viaje.getCapacidad()) {
            viajeClient.marcarViajeCompleto(dto.getNumeroViaje());
        }

        return PasajeResponseDto.fromEntity(guardado);
    }

    public PasajeResponseDto buscarReserva(String documento, String numeroViaje) {
        Pasaje pasaje = pasajeRepository.findByDocumentoPasajeroAndNumeroViaje(documento, numeroViaje)
                .orElseThrow(() -> new BusinessException(
                        "No se encontró la reserva para el DNI " + documento,
                        HttpStatus.NOT_FOUND));
        return PasajeResponseDto.fromEntity(pasaje);
    }

    public List<PasajeResponseDto> historialPorDocumento(String documento) {
        return pasajeRepository.findByDocumentoPasajero(documento).stream()
                .map(PasajeResponseDto::fromEntity)
                .toList();
    }

    public AsientosDisponiblesDto asientosDisponibles(String numeroViaje) {
        ViajeExternoDto viaje = viajeClient.obtenerViaje(numeroViaje);
        List<Integer> ocupados = pasajeRepository.findByNumeroViaje(numeroViaje).stream()
                .map(Pasaje::getAsiento)
                .sorted()
                .toList();

        List<Integer> disponibles = new ArrayList<>();
        IntStream.rangeClosed(1, viaje.getCapacidad())
                .filter(i -> !ocupados.contains(i))
                .forEach(disponibles::add);

        return AsientosDisponiblesDto.builder()
                .numeroViaje(numeroViaje)
                .capacidad(viaje.getCapacidad())
                .ocupados(ocupados)
                .disponibles(disponibles)
                .build();
    }

    public void cancelarPasaje(Long id) {
        Pasaje pasaje = pasajeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pasaje no encontrado", HttpStatus.NOT_FOUND));
        pasajeRepository.delete(pasaje);
    }
}
