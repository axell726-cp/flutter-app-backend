package edu.pe.residencias.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.pe.residencias.model.entity.*;
import edu.pe.residencias.service.*;

@RestController
@RequestMapping("/api/residencias")
public class ResidenciaController {
    
    @Autowired
    private ResidenciaService residenciaService;
    
    @Autowired
    private UniversidadService universidadService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UbicacionService ubicacionService;

    // ----------------------------------------------------------
    // LISTAR TODAS LAS RESIDENCIAS
    // ----------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Residencia>> readAll() {
        List<Residencia> residencias = residenciaService.readAll();
        if (residencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(residencias, HttpStatus.OK);
    }

    // ----------------------------------------------------------
    // LISTAR RESIDENCIAS POR PROPIETARIO
    // ----------------------------------------------------------
    @GetMapping("/propietario/{usuarioId}")
    public ResponseEntity<List<Residencia>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Residencia> residencias = residenciaService.listarPorUsuario(usuarioId);
        if (residencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(residencias, HttpStatus.OK);
    }

    // ----------------------------------------------------------
    // CREAR RESIDENCIA
    // ----------------------------------------------------------
    @PostMapping
    public ResponseEntity<Residencia> crear(@RequestBody Map<String, Object> body) {
        try {
            Long universidadId = ((Number) body.get("universidadId")).longValue();
            Long usuarioId = ((Number) body.get("usuarioId")).longValue();
            Long ubicacionId = ((Number) body.get("ubicacionId")).longValue();

            Universidad universidad = universidadService.read(universidadId)
                    .orElseThrow(() -> new RuntimeException("Universidad no encontrada"));
            Usuario usuario = usuarioService.read(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Ubicacion ubicacion = ubicacionService.read(ubicacionId)
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

            Residencia residencia = new Residencia();
            residencia.setUniversidad(universidad);
            residencia.setUsuario(usuario);
            residencia.setUbicacion(ubicacion);

            residencia.setNombre((String) body.get("nombre"));
            residencia.setTipo((String) body.get("tipo"));
            residencia.setCantidadHabitaciones((Integer) body.get("cantidadHabitaciones"));
            residencia.setReglamentoUrl((String) body.get("reglamentoUrl"));
            residencia.setDescripcion((String) body.get("descripcion"));
            residencia.setTelefonoContacto((String) body.get("telefonoContacto"));
            residencia.setEmailContacto((String) body.get("emailContacto"));
            residencia.setEstado((String) body.get("estado"));
            residencia.setCreatedAt(LocalDateTime.now());

            Residencia saved = residenciaService.create(residencia);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ----------------------------------------------------------
    // LEER RESIDENCIA POR ID
    // ----------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Residencia> getResidenciaId(@PathVariable("id") Long id) {
        return residenciaService.read(id)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ----------------------------------------------------------
    // ELIMINAR RESIDENCIA
    // ----------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delResidencia(@PathVariable("id") Long id) {
        residenciaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ----------------------------------------------------------
    // ACTUALIZAR RESIDENCIA
    // ----------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateResidencia(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        try {
            Residencia residencia = residenciaService.read(id)
                    .orElseThrow(() -> new RuntimeException("Residencia no encontrada"));

            if (body.containsKey("universidadId")) {
                Long universidadId = ((Number) body.get("universidadId")).longValue();
                residencia.setUniversidad(universidadService.read(universidadId)
                        .orElseThrow(() -> new RuntimeException("Universidad no encontrada")));
            }

            if (body.containsKey("usuarioId")) {
                Long usuarioId = ((Number) body.get("usuarioId")).longValue();
                residencia.setUsuario(usuarioService.read(usuarioId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
            }

            if (body.containsKey("ubicacionId")) {
                Long ubicacionId = ((Number) body.get("ubicacionId")).longValue();
                residencia.setUbicacion(ubicacionService.read(ubicacionId)
                        .orElseThrow(() -> new RuntimeException("Ubicación no encontrada")));
            }

            residencia.setNombre((String) body.get("nombre"));
            residencia.setTipo((String) body.get("tipo"));
            residencia.setCantidadHabitaciones((Integer) body.get("cantidadHabitaciones"));
            residencia.setReglamentoUrl((String) body.get("reglamentoUrl"));
            residencia.setDescripcion((String) body.get("descripcion"));
            residencia.setTelefonoContacto((String) body.get("telefonoContacto"));
            residencia.setEmailContacto((String) body.get("emailContacto"));
            residencia.setEstado((String) body.get("estado"));

            Residencia updated = residenciaService.update(residencia);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
