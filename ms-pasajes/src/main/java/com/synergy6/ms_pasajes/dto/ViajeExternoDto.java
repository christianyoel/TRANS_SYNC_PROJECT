package com.synergy6.ms_pasajes.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ViajeExternoDto {

    private Long id;
    private String numeroViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;
    private Double precio;
    private Integer capacidad;
    private String estado;
}
