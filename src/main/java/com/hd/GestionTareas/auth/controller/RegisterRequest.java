package com.hd.GestionTareas.auth.controller;

public record RegisterRequest(
        String nombres,
        String apellidos,
        String email,
        String password
) {
}
