package com.synergy6.ms_encomiendas.repository;

import com.synergy6.ms_encomiendas.model.Encomienda;
import com.synergy6.ms_encomiendas.model.EstadoEncomienda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncomiendaRepository extends JpaRepository<Encomienda, Long> {

    /** Todas las encomiendas ordenadas de más reciente a más antigua — con paginación. */
    Page<Encomienda> findAllByOrderByFechaDesc(Pageable pageable);

    /** Filtrar por estado con paginación. */
    Page<Encomienda> findByEstadoOrderByFechaDesc(EstadoEncomienda estado, Pageable pageable);

    /** Devuelve conteos por estado para el Dashboard. */
    List<Encomienda> findByEstadoOrderByFechaDesc(EstadoEncomienda estado);

    /** Conteo por estado para el resumen del Dashboard. */
    long countByEstado(EstadoEncomienda estado);

    /** Búsqueda de texto libre en remitente o destinatario (case-insensitive). */
    @Query("SELECT e FROM Encomienda e WHERE " +
           "LOWER(e.remitente) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(e.destinatario) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "ORDER BY e.fecha DESC")
    List<Encomienda> buscarPorTexto(@Param("q") String q);

    /** Búsqueda pública por remitente exacto (para seguimiento sin autenticación). */
    List<Encomienda> findByRemitenteContainingIgnoreCaseOrderByFechaDesc(String remitente);

    /** Búsqueda pública por destinatario (para seguimiento sin autenticación). */
    List<Encomienda> findByDestinatarioContainingIgnoreCaseOrderByFechaDesc(String destinatario);
}
