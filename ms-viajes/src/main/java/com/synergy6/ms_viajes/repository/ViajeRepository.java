package com.synergy6.ms_viajes.repository;

import com.synergy6.ms_viajes.model.Viaje;
import com.synergy6.ms_viajes.model.ViajeEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    Optional<Viaje> findByNumeroViaje(String numeroViaje);

    boolean existsByNumeroViaje(String numeroViaje);

    List<Viaje> findByEstado(ViajeEstado estado);
}
