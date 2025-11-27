package edu.pe.residencias.service;

import java.util.List;
import java.util.Optional;

import edu.pe.residencias.model.entity.VistoRecientemente;

public interface VistoRecientementeService {

    VistoRecientemente create(VistoRecientemente vistoRecientemente);
    VistoRecientemente update(VistoRecientemente vistoRecientemente);
    void delete(Long id);
    Optional<VistoRecientemente> read(Long id);
    List<VistoRecientemente> readAll();

    // Métodos específicos
    List<VistoRecientemente> obtenerVistosPorUsuario(Long usuarioId);
    List<VistoRecientemente> obtenerUltimos10VistosPorUsuario(Long usuarioId);
    VistoRecientemente registrarVista(Long usuarioId, Long residenciaId);
    void eliminarVistosPorUsuario(Long usuarioId);
}