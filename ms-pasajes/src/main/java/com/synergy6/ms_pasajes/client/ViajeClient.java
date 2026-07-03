package com.synergy6.ms_pasajes.client;

import com.synergy6.ms_pasajes.dto.ViajeExternoDto;
import com.synergy6.ms_pasajes.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class ViajeClient {

    private final RestClient restClient;

    public ViajeClient(@Value("${ms-viajes.url}") String viajesUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(viajesUrl)
                .build();
    }

    public ViajeExternoDto obtenerViaje(String numeroViaje) {
        try {
            return restClient.get()
                    .uri("/api/viajes/{numeroViaje}", numeroViaje)
                    .retrieve()
                    .body(ViajeExternoDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException("Viaje no encontrado: " + numeroViaje, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new BusinessException("No se pudo consultar el servicio de viajes");
        }
    }

    public void marcarViajeCompleto(String numeroViaje) {
        try {
            restClient.patch()
                    .uri("/api/viajes/{numeroViaje}/completo", numeroViaje)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ignored) {
            // No bloquear la venta si falla la actualización del estado del viaje
        }
    }
}
