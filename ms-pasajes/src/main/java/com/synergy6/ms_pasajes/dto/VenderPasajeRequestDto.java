package com.synergy6.ms_pasajes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VenderPasajeRequestDto {

    @NotBlank(message = "El número de viaje es obligatorio")
    private String numeroViaje;

    @NotNull(message = "El asiento es obligatorio")
    @Min(value = 1, message = "El asiento mínimo es 1")
    private Integer asiento;

    @NotBlank(message = "El documento del pasajero es obligatorio")
    @Size(max = 15)
    private String documentoPasajero;

    @NotBlank(message = "El nombre del pasajero es obligatorio")
    @Size(max = 100)
    private String nombrePasajero;
}
