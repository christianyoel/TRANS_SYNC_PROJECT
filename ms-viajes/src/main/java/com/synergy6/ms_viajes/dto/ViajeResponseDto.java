package com.synergy6.ms_viajes.dto;

import com.synergy6.ms_viajes.model.Viaje;
import com.synergy6.ms_viajes.model.ViajeEstado;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViajeResponseDto {

    private Long id;
    private String numeroViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;
    private Double precio;
    private Integer capacidad;
    private ViajeEstado estado;
    private LocalDateTime fechaCreacion;

    public static ViajeResponseDto fromEntity(Viaje viaje) {
        return ViajeResponseDto.builder()
                .id(viaje.getId())
                .numeroViaje(viaje.getNumeroViaje())
                .origen(viaje.getOrigen())
                .destino(viaje.getDestino())
                .fechaSalida(viaje.getFechaSalida())
                .precio(viaje.getPrecio())
                .capacidad(viaje.getCapacidad())
                .estado(viaje.getEstado())
                .fechaCreacion(viaje.getFechaCreacion())
                .build();
    }
}
