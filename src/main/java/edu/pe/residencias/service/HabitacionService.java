package edu.pe.residencias.service;

import java.util.List;
import java.util.Optional;

import edu.pe.residencias.model.entity.Habitacion;

public interface HabitacionService {
    Habitacion create(Habitacion habitacion);
    Habitacion update(Habitacion habitacion);
    void delete(Long id);
    Optional<Habitacion> read(Long id);
    List<Habitacion> readAll();

    // Nuevos métodos
    List<Habitacion> listarPorResidencia(Long residenciaId);
    List<Habitacion> listarDestacadasPorResidencia(Long residenciaId);
    List<Habitacion> listarDisponiblesPorResidencia(Long residenciaId);
    List<Habitacion> listarPorEstado(String estado);
}
