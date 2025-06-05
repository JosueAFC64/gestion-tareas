package com.hd.GestionTareas.curso.controller;

import java.util.List;

public record CursoRequest(
        String codigo,
        String nombre,
        List<Long> docentesAsignadosIds
) {
}
