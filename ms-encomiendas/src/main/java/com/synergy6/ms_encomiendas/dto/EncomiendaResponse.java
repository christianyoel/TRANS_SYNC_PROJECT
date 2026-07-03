package com.synergy6.ms_encomiendas.dto;

import com.synergy6.ms_encomiendas.model.Encomienda;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EncomiendaResponse {

    private Long id;
    private String remitente;
    private String destinatario;
    private String origen;
    private String destino;
    private BigDecimal peso;
    private BigDecimal precio;
    private EstadoEncomienda estado;
    private LocalDateTime fecha;

    public static EncomiendaResponse fromEntity(Encomienda encomienda) {
        return EncomiendaResponse.builder()
                .id(encomienda.getId())
                .remitente(encomienda.getRemitente())
                .destinatario(encomienda.getDestinatario())
                .origen(encomienda.getOrigen())
                .destino(encomienda.getDestino())
                .peso(encomienda.getPeso())
                .precio(encomienda.getPrecio())
                .estado(encomienda.getEstado())
                .fecha(encomienda.getFecha())
                .build();
    }
}
