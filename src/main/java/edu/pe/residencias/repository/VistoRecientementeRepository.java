package edu.pe.residencias.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.pe.residencias.model.entity.VistoRecientemente;

@Repository
public interface VistoRecientementeRepository extends JpaRepository<VistoRecientemente, Long> {

    // Obtener vistos recientemente por usuario ordenados por fecha más reciente
    @Query("SELECT v FROM VistoRecientemente v WHERE v.usuario.id = :usuarioId ORDER BY v.fechaVista DESC")
    List<VistoRecientemente> findByUsuarioIdOrderByFechaVistaDesc(@Param("usuarioId") Long usuarioId);

    // Verificar si ya existe un registro de vista para un usuario y residencia específicos
    Optional<VistoRecientemente> findByUsuarioIdAndResidenciaId(Long usuarioId, Long residenciaId);

    // Obtener los últimos N vistos recientemente por usuario
    @Query("SELECT v FROM VistoRecientemente v WHERE v.usuario.id = :usuarioId ORDER BY v.fechaVista DESC")
    List<VistoRecientemente> findTop10ByUsuarioIdOrderByFechaVistaDesc(@Param("usuarioId") Long usuarioId);
}