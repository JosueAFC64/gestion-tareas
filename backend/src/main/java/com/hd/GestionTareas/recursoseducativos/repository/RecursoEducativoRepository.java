package com.hd.GestionTareas.recursoseducativos.repository;

import com.hd.GestionTareas.TipoRecurso;
import com.hd.GestionTareas.recursoseducativos.controller.RESummaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface RecursoEducativoRepository extends JpaRepository<RecursosEducativo, Long> {
    List<RecursosEducativo> findByCurso_Id(Long cursoId);

    @Query("""
    SELECT new com.hd.GestionTareas.recursoseducativos.controller.RESummaryResponse(
        r.id,
        r.titulo,
        r.descripcion,
        r.tipo,
        r.url,
        r.creador.nombres,
        r.curso.nombre,
        r.categoria.nombre,
        r.fechaCreacion
    )
    FROM RecursosEducativo r
    WHERE (:titulo IS NULL OR LOWER(r.titulo) LIKE CONCAT('%', LOWER(CAST(:titulo AS string)), '%'))
      AND (:tipo IS NULL OR r.tipo = :tipo)
      AND (:cursoId IS NULL OR r.curso.id = :cursoId)
      AND (:creadorId IS NULL OR r.creador.id = :creadorId)
      AND (:categoriaId IS NULL OR r.categoria.id = :categoriaId)
""")
    List<RESummaryResponse> filtrarResumen(
            @Param("titulo") String titulo,
            @Param("tipo") TipoRecurso tipo,
            @Param("cursoId") Long cursoId,
            @Param("creadorId") Long creadorId,
            @Param("categoriaId") Long categoriaId
    );

}
