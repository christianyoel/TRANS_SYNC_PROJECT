package com.synergy6.ms_pasajes.repository;

import com.synergy6.ms_pasajes.model.Pasaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasajeRepository extends JpaRepository<Pasaje, Long> {

    Optional<Pasaje> findByDocumentoPasajeroAndNumeroViaje(String documentoPasajero, String numeroViaje);

    boolean existsByNumeroViajeAndAsiento(String numeroViaje, Integer asiento);

    List<Pasaje> findByDocumentoPasajero(String documentoPasajero);

    List<Pasaje> findByNumeroViaje(String numeroViaje);

    long countByNumeroViaje(String numeroViaje);
}
