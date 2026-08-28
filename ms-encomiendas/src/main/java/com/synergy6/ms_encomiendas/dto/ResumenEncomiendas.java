package com.synergy6.ms_encomiendas.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de resumen para el Dashboard del Admin.
 * Devuelve conteos agrupados por estado y el total general.
 */
@Data
@Builder
public class ResumenEncomiendas {
    private long total;
    private long registradas;
    private long enTransito;
    private long entregadas;
    private long canceladas;
}
