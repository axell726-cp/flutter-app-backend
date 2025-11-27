package edu.pe.residencias.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.residencias.model.entity.VistoRecientemente;
import edu.pe.residencias.service.VistoRecientementeService;

@RestController
@RequestMapping("/api/vistos-recientemente")
public class VistoRecientementeController {

    @Autowired
    private VistoRecientementeService vistoRecientementeService;

    // ============================
    // OBTENER VISTOS RECIENTEMENTE POR USUARIO
    // ============================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<VistoRecientemente>> obtenerVistosPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<VistoRecientemente> vistos = vistoRecientementeService.obtenerVistosPorUsuario(usuarioId);
            
            if (vistos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(vistos, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // OBTENER ÚLTIMOS 10 VISTOS RECIENTEMENTE
    // ============================
    @GetMapping("/usuario/{usuarioId}/ultimos")
    public ResponseEntity<List<VistoRecientemente>> obtenerUltimos10Vistos(@PathVariable Long usuarioId) {
        try {
            List<VistoRecientemente> vistos = vistoRecientementeService.obtenerUltimos10VistosPorUsuario(usuarioId);
            
            if (vistos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(vistos, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // REGISTRAR VISTA DE RESIDENCIA
    // ============================
    @PostMapping("/registrar")
    public ResponseEntity<VistoRecientemente> registrarVista(@RequestBody Map<String, Object> body) {
        try {
            Long usuarioId = ((Number) body.get("usuarioId")).longValue();
            Long residenciaId = ((Number) body.get("residenciaId")).longValue();
            
            VistoRecientemente visto = vistoRecientementeService.registrarVista(usuarioId, residenciaId);
            
            return new ResponseEntity<>(visto, HttpStatus.CREATED);
            
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // ELIMINAR TODOS LOS VISTOS DE UN USUARIO
    // ============================
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<String> eliminarVistosPorUsuario(@PathVariable Long usuarioId) {
        try {
            vistoRecientementeService.eliminarVistosPorUsuario(usuarioId);
            return new ResponseEntity<>("Historial de vistos eliminado correctamente", HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar historial", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // ELIMINAR VISTA ESPECÍFICA
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVista(@PathVariable Long id) {
        try {
            vistoRecientementeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}