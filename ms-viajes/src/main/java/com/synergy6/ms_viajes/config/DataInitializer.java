package com.synergy6.ms_viajes.config;

import com.synergy6.ms_viajes.model.Viaje;
import com.synergy6.ms_viajes.model.ViajeEstado;
import com.synergy6.ms_viajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ViajeRepository viajeRepository;

    @Override
    public void run(String... args) {
        seedOrUpdate("JUL-CUS-1024", "Juliaca", "Cusco", hoy(6, 30), 45.0);
        seedOrUpdate("JUL-PUN-0800", "Juliaca", "Puno", hoy(8, 0), 35.0);
        seedOrUpdate("JUL-LIM-1200", "Juliaca", "Lima", hoy(12, 0), 85.0);
        seedOrUpdate("CUS-ARE-0512", "Cusco", "Arequipa", hoy(14, 15), 55.0);
        seedOrUpdate("CUS-JUL-1700", "Cusco", "Juliaca", hoy(17, 0), 45.0);
        seedOrUpdate("LIM-JUL-2200", "Lima", "Juliaca", hoy(22, 0), 90.0);

        viajeRepository.findByNumeroViaje("JUL-LIM-2048").ifPresent(v -> {
            v.setEstado(ViajeEstado.CANCELADO);
            viajeRepository.save(v);
        });
    }

    private LocalDateTime hoy(int hora, int minuto) {
        return LocalDate.now().atTime(LocalTime.of(hora, minuto));
    }

    private void seedOrUpdate(String numeroViaje, String origen, String destino,
                               LocalDateTime fechaSalida, double precio) {
        Viaje viaje = viajeRepository.findByNumeroViaje(numeroViaje)
                .orElseGet(() -> {
                    Viaje nuevo = new Viaje();
                    nuevo.setNumeroViaje(numeroViaje);
                    return nuevo;
                });

        viaje.setOrigen(origen);
        viaje.setDestino(destino);
        viaje.setFechaSalida(fechaSalida);
        viaje.setPrecio(precio);
        viaje.setCapacidad(7);
        viaje.setEstado(ViajeEstado.ACTIVO);
        viajeRepository.save(viaje);
    }
}
