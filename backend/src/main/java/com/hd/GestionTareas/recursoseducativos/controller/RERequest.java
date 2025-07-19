package com.hd.GestionTareas.recursoseducativos.controller;

import com.hd.GestionTareas.TipoRecurso;

public record RERequest(
        String titulo,
        String descripcion,
        TipoRecurso tipo,
        String url,
        Long cursoId,
        Long categoriaId
) {
}
