package edu.pe.residencias.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.pe.residencias.model.entity.Persona;
import edu.pe.residencias.model.entity.Usuario;
import edu.pe.residencias.repository.UsuarioRepository;
import edu.pe.residencias.service.PersonaService;
import edu.pe.residencias.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario create(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> read(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Usuario> readAll() {
        return repository.findAll();
    }

    // ===================================
    // PATCH USUARIO
    // ===================================
    @Override
    public Usuario patchUsuario(Long id, Map<String, Object> cambios) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        cambios.forEach((key, value) -> {
            switch (key) {
                case "username":
                    usuario.setUsername((String) value);
                    break;

                case "estado":
                    usuario.setEstado((String) value);
                    break;

                case "password":
                    usuario.setPassword(passwordEncoder.encode(value.toString()));
                    break;

                case "emailVerificado":
                    usuario.setEmailVerificado(Boolean.parseBoolean(value.toString()));
                    break;

                case "rolId":
                    // se obtiene desde PersonaService si lo necesitas implementar
                    break;

                default:
                    break;
            }
        });

        return repository.save(usuario);
    }

    // ===================================
    // PATCH PERSONA DEL USUARIO
    // ===================================
    @Override
    public Persona patchPersona(Long idUsuario, Map<String, Object> cambios) {

        Usuario usuario = repository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Persona persona = usuario.getPersona();

        cambios.forEach((k, v) -> {
            switch (k) {
                case "nombre":
                    persona.setNombre(v.toString());
                    break;
                case "apellido":
                    persona.setApellido(v.toString());
                    break;
                case "dni":
                    persona.setDni(v.toString());
                    break;
                case "telefono":
                    persona.setTelefono(v.toString());
                    break;
            }
        });

        return personaService.update(persona);
    }

    // ===================================
    // CAMBIAR PASSWORD
    // ===================================
    @Override
    public void cambiarPassword(Long id, String passwordActual, String passwordNueva) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        repository.save(usuario);
    }
}
