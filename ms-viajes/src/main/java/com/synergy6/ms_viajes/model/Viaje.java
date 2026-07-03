package com.synergy6.ms_viajes.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_viaje", unique = true, nullable = false, length = 20)
    private String numeroViaje;

    @Column(nullable = false, length = 50)
    private String origen;

    @Column(nullable = false, length = 50)
    private String destino;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer capacidad = 7;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ViajeEstado estado = ViajeEstado.ACTIVO;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
