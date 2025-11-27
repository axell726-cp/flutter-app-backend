package edu.pe.residencias.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.residencias.model.entity.Habitacion;
import edu.pe.residencias.model.entity.Residencia;
import edu.pe.residencias.service.HabitacionService;
import edu.pe.residencias.service.ResidenciaService;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {
    
    @Autowired
    private HabitacionService habitacionService;
    
    @Autowired
    private ResidenciaService residenciaService;

    @GetMapping
    public ResponseEntity<List<Habitacion>> readAll() {
        try {
            List<Habitacion> habitaciones = habitacionService.readAll();
            if (habitaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(habitaciones, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Habitacion> crear(@RequestBody Map<String, Object> body) {
        try {
            Long residenciaId = ((Number) body.get("residenciaId")).longValue();

            Residencia residencia = residenciaService.read(residenciaId)
                    .orElseThrow(() -> new RuntimeException("Residencia no encontrada"));

            Habitacion habitacion = new Habitacion();
            habitacion.setResidencia(residencia);

            habitacion.setNombre((String) body.get("nombre"));
            habitacion.setCodigoHabitacion((String) body.get("codigoHabitacion"));

            if (body.containsKey("precioMensual")) {
                Number precio = (Number) body.get("precioMensual");
                habitacion.setPrecioMensual(BigDecimal.valueOf(precio.doubleValue()));
            }
            
            if (body.containsKey("capacidad")) {
                Number capacidad = (Number) body.get("capacidad");
                habitacion.setCapacidad(capacidad.intValue());
            }

            habitacion.setEstado((String) body.get("estado"));
            habitacion.setCreatedAt(LocalDateTime.now());

            Habitacion h = habitacionService.create(habitacion);
            return new ResponseEntity<>(h, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> getHabitacionId(@PathVariable("id") Long id) {
        try {
            Habitacion h = habitacionService.read(id).get();
            return new ResponseEntity<>(h, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Habitacion> delHabitacion(@PathVariable("id") Long id) {
        try {
            habitacionService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabitacion(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Habitacion habitacion = habitacionService.read(id)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

            if (body.containsKey("residenciaId")) {
                Long residenciaId = ((Number) body.get("residenciaId")).longValue();
                habitacion.setResidencia(
                    residenciaService.read(residenciaId)
                        .orElseThrow(() -> new RuntimeException("Residencia no encontrada"))
                );
            }

            habitacion.setNombre((String) body.get("nombre"));
            habitacion.setCodigoHabitacion((String) body.get("codigoHabitacion"));

            if (body.containsKey("precioMensual")) {
                Number precio = (Number) body.get("precioMensual");
                habitacion.setPrecioMensual(BigDecimal.valueOf(precio.doubleValue()));
            }

            if (body.containsKey("capacidad")) {
                Number capacidad = (Number) body.get("capacidad");
                habitacion.setCapacidad(capacidad.intValue());
            }

            habitacion.setEstado((String) body.get("estado"));

            Habitacion updated = habitacionService.update(habitacion);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // LISTAR HABITACIONES POR RESIDENCIA
    // ============================
    @GetMapping("/residencia/{residenciaId}")
    public ResponseEntity<List<Habitacion>> listarPorResidencia(@PathVariable Long residenciaId) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarPorResidencia(residenciaId);
            
            if (habitaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(habitaciones, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // LISTAR HABITACIONES DESTACADAS POR RESIDENCIA
    // ============================
    @GetMapping("/residencia/{residenciaId}/destacadas")
    public ResponseEntity<List<Habitacion>> listarDestacadasPorResidencia(@PathVariable Long residenciaId) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarDestacadasPorResidencia(residenciaId);
            
            if (habitaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(habitaciones, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // LISTAR HABITACIONES DISPONIBLES POR RESIDENCIA
    // ============================
    @GetMapping("/residencia/{residenciaId}/disponibles")
    public ResponseEntity<List<Habitacion>> listarDisponiblesPorResidencia(@PathVariable Long residenciaId) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarDisponiblesPorResidencia(residenciaId);
            
            if (habitaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(habitaciones, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================
    // LISTAR HABITACIONES POR ESTADO
    // ============================
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Habitacion>> listarPorEstado(@PathVariable String estado) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarPorEstado(estado);
            
            if (habitaciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(habitaciones, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
