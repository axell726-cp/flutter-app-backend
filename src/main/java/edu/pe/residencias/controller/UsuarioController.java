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

import edu.pe.residencias.model.entity.Persona;
import edu.pe.residencias.model.entity.Rol;
import edu.pe.residencias.model.entity.Usuario;
import edu.pe.residencias.service.PersonaService;
import edu.pe.residencias.service.RolService;
import edu.pe.residencias.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<Usuario>> readAll() {
        try {
            List<Usuario> usuarios = usuarioService.readAll();
            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Map<String, Object> body) {
        try {
            Long personaId = ((Number) body.get("personaId")).longValue();
            Long rolId = ((Number) body.get("rolId")).longValue();

            Persona persona = personaService.read(personaId)
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

            Rol rol = rolService.read(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            Usuario usuario = new Usuario();
            usuario.setPersona(persona);
            usuario.setRol(rol);
            usuario.setUsername((String) body.get("username"));
            usuario.setPassword((String) body.get("password"));
            usuario.setEstado((String) body.get("estado"));
            usuario.setEmailVerificado(false);
            usuario.setCreatedAt(LocalDateTime.now());

            Usuario saved = usuarioService.create(usuario);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioId(@PathVariable("id") Long id) {
        try {
            Usuario u = usuarioService.read(id).get();
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> delUsuario(@PathVariable("id") Long id) {
        try {
            usuarioService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        
        Usuario usuario = usuarioService.read(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long personaId = ((Number) body.get("personaId")).longValue();
        Long rolId = ((Number) body.get("rolId")).longValue();

        Persona persona = personaService.read(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        Rol rol = rolService.read(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setPersona(persona);
        usuario.setRol(rol);
        usuario.setUsername((String) body.get("username"));
        usuario.setPassword((String) body.get("password"));
        usuario.setEstado((String) body.get("estado"));

        Usuario updated = usuarioService.update(usuario);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
