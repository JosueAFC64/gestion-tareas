package com.hd.GestionTareas.user.controller;

public record UserDataResponse(
        Long id,
        String nombres,
        String apellidos,
        String email,
        String rol
) {
}
