package edu.pe.residencias.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.pe.residencias.model.entity.Persona;
import edu.pe.residencias.model.entity.Usuario;

public interface UsuarioService {

    Usuario create(Usuario usuario);
    Usuario update(Usuario usuario);
    void delete(Long id);
    Optional<Usuario> read(Long id);
    List<Usuario> readAll();

    // Nuevos métodos
    Usuario patchUsuario(Long id, Map<String, Object> cambios);
    Persona patchPersona(Long idUsuario, Map<String, Object> cambios);
    void cambiarPassword(Long id, String passwordActual, String passwordNueva);
}
