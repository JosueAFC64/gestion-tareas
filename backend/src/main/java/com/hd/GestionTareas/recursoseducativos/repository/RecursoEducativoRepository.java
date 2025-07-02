package com.hd.GestionTareas.recursoseducativos.repository;

import com.hd.GestionTareas.recursoseducativos.controller.RESummaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoEducativoRepository extends JpaRepository<RecursosEducativo, Long> {
    List<RecursosEducativo> findByCurso_Id(Long cursoId);

    List<RESummaryResponse> findByTituloContainingIgnoreCase(String titulo);
}
