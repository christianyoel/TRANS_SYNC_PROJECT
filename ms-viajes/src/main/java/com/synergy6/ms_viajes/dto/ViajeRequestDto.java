package com.synergy6.ms_viajes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ViajeRequestDto {

    @NotBlank(message = "El número de viaje es obligatorio")
    @Size(max = 20)
    private String numeroViaje;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 50)
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 50)
    private String destino;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime fechaSalida;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @Min(value = 1, message = "La capacidad mínima es 1")
    @Max(value = 50, message = "La capacidad máxima es 50")
    private Integer capacidad = 7;
}
