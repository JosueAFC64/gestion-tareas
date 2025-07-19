package com.hd.GestionTareas.recursoseducativos.controller;

import com.hd.GestionTareas.TipoRecurso;

import java.time.LocalDateTime;

public record RESummaryResponse(
        Long id,
        String titulo,
        String descripcion,
        TipoRecurso tipo,
        String url,
        String creador,
        String curso,
        String nombreCategoria,
        LocalDateTime fechaCreacion
) {}