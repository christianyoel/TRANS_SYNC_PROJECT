package com.synergy6.ms_pasajes.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pasajes")
public class Pasaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_viaje", nullable = false, length = 20)
    private String numeroViaje; // Ej: JUL-CUS-1024

    @Column(nullable = false, length = 50)
    private String origen; // Ej: Juliaca

    @Column(nullable = false, length = 50)
    private String destino; // Ej: Cusco

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(nullable = false)
    private Integer asiento; // Del 1 al 7 (Capacidad de la minivan)

    @Column(name = "documento_pasajero", nullable = false, length = 15)
    private String documentoPasajero;

    @Column(name = "nombre_pasajero", nullable = false, length = 100)
    private String nombrePasajero;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "fecha_compra", updatable = false)
    private LocalDateTime fechaCompra = LocalDateTime.now();
}