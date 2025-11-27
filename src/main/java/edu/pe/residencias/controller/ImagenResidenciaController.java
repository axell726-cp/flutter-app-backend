package edu.pe.residencias.controller;

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

import jakarta.validation.Valid;
import edu.pe.residencias.model.entity.ImagenResidencia;
import edu.pe.residencias.model.entity.Residencia;
import edu.pe.residencias.service.ImagenResidenciaService;
import edu.pe.residencias.service.ResidenciaService;

@RestController
@RequestMapping("/api/imagenes-residencias")
public class ImagenResidenciaController {
    
    @Autowired
    private ImagenResidenciaService imagenResidenciaService;
    
    @Autowired
    private ResidenciaService residenciaService;

    @GetMapping
    public ResponseEntity<List<ImagenResidencia>> readAll() {
        try {
            List<ImagenResidencia> imagenes = imagenResidenciaService.readAll();
            if (imagenes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(imagenes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long residenciaId = ((Number) body.get("residenciaId")).longValue();

            Residencia residencia = residenciaService.read(residenciaId)
                    .orElseThrow(() -> new RuntimeException("Residencia no encontrada"));

            ImagenResidencia imagen = new ImagenResidencia();
            imagen.setResidencia(residencia);
            imagen.setUrl((String) body.get("url"));
            imagen.setOrden((Integer) body.get("orden"));
            imagen.setEstado((String) body.get("estado"));
            imagen.setCreatedAt(LocalDateTime.now());

            ImagenResidencia creada = imagenResidenciaService.create(imagen);

            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ImagenResidencia> getImagenResidenciaId(@PathVariable("id") Long id) {
        try {
            ImagenResidencia i = imagenResidenciaService.read(id).get();
            return new ResponseEntity<>(i, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ImagenResidencia> delImagenResidencia(@PathVariable("id") Long id) {
        try {
            imagenResidenciaService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImagenResidencia(@PathVariable("id") Long id, @Valid @RequestBody ImagenResidencia body) {
        try {
            ImagenResidencia imagen = imagenResidenciaService.read(id)
                    .orElseThrow(() -> new RuntimeException("ImagenResidencia no encontrada"));

            imagen.setUrl(body.getUrl());
            imagen.setOrden(body.getOrden());
            imagen.setEstado(body.getEstado());

            if (body.getResidencia() != null && body.getResidencia().getId() != null) {
                Residencia residencia = residenciaService.read(body.getResidencia().getId())
                        .orElseThrow(() -> new RuntimeException("Residencia no encontrada"));
                imagen.setResidencia(residencia);
            }

            ImagenResidencia updated = imagenResidenciaService.update(imagen);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
