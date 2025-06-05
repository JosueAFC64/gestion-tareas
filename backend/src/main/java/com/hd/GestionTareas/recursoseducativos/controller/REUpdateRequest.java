package com.hd.GestionTareas.recursoseducativos.controller;

import com.hd.GestionTareas.TipoRecurso;

public record REUpdateRequest(
        String titulo,
        String descripcion,
        TipoRecurso tipo,
        String url
) {
}
