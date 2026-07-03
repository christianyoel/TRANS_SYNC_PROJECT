package com.synergy6.ms_pasajes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AsientosDisponiblesDto {

    private String numeroViaje;
    private Integer capacidad;
    private List<Integer> ocupados;
    private List<Integer> disponibles;
}
