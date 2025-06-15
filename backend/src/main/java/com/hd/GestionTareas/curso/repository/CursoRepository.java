package com.hd.GestionTareas.curso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNombre(String nombre);

    boolean existsByCodigo(String codigo);
}
