package edu.pe.residencias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.pe.residencias.model.entity.Residencia;

@Repository
public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {

    // Listar residencias creadas por un usuario propietario
    List<Residencia> findByUsuarioId(Long usuarioId);
}
