package com.synergy6.ms_encomiendas.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Wrapper genérico de paginación.
 * Devuelve siempre la misma forma de respuesta para cualquier listado paginado:
 * { content, page, size, totalElements, totalPages, last }
 */
@Data
@Builder
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static <E, D> PageResponse<D> of(Page<E> springPage, Function<E, D> mapper) {
        return PageResponse.<D>builder()
                .content(springPage.getContent().stream().map(mapper).toList())
                .page(springPage.getNumber())
                .size(springPage.getSize())
                .totalElements(springPage.getTotalElements())
                .totalPages(springPage.getTotalPages())
                .last(springPage.isLast())
                .build();
    }
}
