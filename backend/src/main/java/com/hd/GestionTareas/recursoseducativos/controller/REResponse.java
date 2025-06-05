package com.hd.GestionTareas.recursoseducativos.controller;

import com.hd.GestionTareas.TipoRecurso;

import java.time.LocalDateTime;

public record REResponse(
        Long id,
        String titulo,
        String descripcion,
        TipoRecurso tipo,
        String url,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaUltimaModificacion,
        String nombreCreador,
        String nombreCurso
) {
}
