package com.hd.GestionTareas.curso.controller;

import java.util.List;

public record CursoResponse(
        Long id,
        String codigo,
        String nombre,
        String docente
) {
}