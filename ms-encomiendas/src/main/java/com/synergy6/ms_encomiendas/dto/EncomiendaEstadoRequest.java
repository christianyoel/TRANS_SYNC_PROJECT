package com.synergy6.ms_encomiendas.dto;

import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EncomiendaEstadoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoEncomienda estado;
}
