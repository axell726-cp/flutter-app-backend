package edu.pe.residencias.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pe.residencias.model.entity.Residencia;
import edu.pe.residencias.model.entity.Usuario;
import edu.pe.residencias.model.entity.VistoRecientemente;
import edu.pe.residencias.repository.VistoRecientementeRepository;
import edu.pe.residencias.service.ResidenciaService;
import edu.pe.residencias.service.UsuarioService;
import edu.pe.residencias.service.VistoRecientementeService;

@Service
@Transactional
public class VistoRecientementeServiceImpl implements VistoRecientementeService {

    @Autowired
    private VistoRecientementeRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResidenciaService residenciaService;

    @Override
    public VistoRecientemente create(VistoRecientemente vistoRecientemente) {
        vistoRecientemente.setCreatedAt(LocalDateTime.now());
        return repository.save(vistoRecientemente);
    }

    @Override
    public VistoRecientemente update(VistoRecientemente vistoRecientemente) {
        return repository.save(vistoRecientemente);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<VistoRecientemente> read(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<VistoRecientemente> readAll() {
        return repository.findAll();
    }

    @Override
    public List<VistoRecientemente> obtenerVistosPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByFechaVistaDesc(usuarioId);
    }

    @Override
    public List<VistoRecientemente> obtenerUltimos10VistosPorUsuario(Long usuarioId) {
        List<VistoRecientemente> vistos = repository.findTop10ByUsuarioIdOrderByFechaVistaDesc(usuarioId);
        return vistos.size() > 10 ? vistos.subList(0, 10) : vistos;
    }

    @Override
    public VistoRecientemente registrarVista(Long usuarioId, Long residenciaId) {
        // Verificar si ya existe un registro
        Optional<VistoRecientemente> existente = repository.findByUsuarioIdAndResidenciaId(usuarioId, residenciaId);
        
        if (existente.isPresent()) {
            // Actualizar la fecha de vista
            VistoRecientemente visto = existente.get();
            visto.setFechaVista(LocalDateTime.now());
            return repository.save(visto);
        } else {
            // Crear nuevo registro
            Usuario usuario = usuarioService.read(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Residencia residencia = residenciaService.read(residenciaId)
                    .orElseThrow(() -> new RuntimeException("Residencia no encontrada"));
            
            VistoRecientemente nuevoVisto = new VistoRecientemente();
            nuevoVisto.setUsuario(usuario);
            nuevoVisto.setResidencia(residencia);
            nuevoVisto.setFechaVista(LocalDateTime.now());
            nuevoVisto.setCreatedAt(LocalDateTime.now());
            
            return repository.save(nuevoVisto);
        }
    }

    @Override
    public void eliminarVistosPorUsuario(Long usuarioId) {
        List<VistoRecientemente> vistos = repository.findByUsuarioIdOrderByFechaVistaDesc(usuarioId);
        repository.deleteAll(vistos);
    }
}