package com.hd.GestionTareas.error;

public record ErrorResponse(
        int status,
        String error,
        String message
) {
}