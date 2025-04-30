package com.hd.GestionTareas.auth.controller;

public record AuthRequest(
        String email,
        String password
) {
}
