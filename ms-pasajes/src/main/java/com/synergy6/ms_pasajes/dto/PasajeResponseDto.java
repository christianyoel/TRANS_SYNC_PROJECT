package com.synergy6.ms_pasajes.dto;

import com.synergy6.ms_pasajes.model.Pasaje;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class PasajeResponseDto {

    private Long id;
    private String numeroViaje;
    private String nombreCompleto;
    private String origen;
    private String destino;
    private Integer asiento;
    private String fechaViaje;
    private String documentoPasajero;
    private Double precio;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");

    public static PasajeResponseDto fromEntity(Pasaje pasaje) {
        return PasajeResponseDto.builder()
                .id(pasaje.getId())
                .numeroViaje(pasaje.getNumeroViaje())
                .nombreCompleto(pasaje.getNombrePasajero())
                .origen(pasaje.getOrigen())
                .destino(pasaje.getDestino())
                .asiento(pasaje.getAsiento())
                .fechaViaje(pasaje.getFechaSalida() != null
                        ? pasaje.getFechaSalida().format(FORMATTER)
                        : null)
                .documentoPasajero(pasaje.getDocumentoPasajero())
                .precio(pasaje.getPrecio())
                .build();
    }
}
