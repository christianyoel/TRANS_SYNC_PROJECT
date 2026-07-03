package com.synergy6.ms_encomiendas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "encomiendas")
@Getter
@Setter
@NoArgsConstructor
public class Encomienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String remitente;

    @Column(nullable = false, length = 120)
    private String destinatario;

    @Column(nullable = false, length = 80)
    private String origen;

    @Column(nullable = false, length = 80)
    private String destino;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal peso;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEncomienda estado = EstadoEncomienda.REGISTRADA;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
