package edu.pe.residencias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.pe.residencias.model.entity.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    // Listar habitaciones por residencia
    List<Habitacion> findByResidenciaId(Long residenciaId);

    // Listar habitaciones destacadas por residencia
    List<Habitacion> findByResidenciaIdAndDestacadoTrue(Long residenciaId);

    // Listar habitaciones por estado
    List<Habitacion> findByEstado(String estado);

    // Listar habitaciones disponibles por residencia
    List<Habitacion> findByResidenciaIdAndEstado(Long residenciaId, String estado);
}
