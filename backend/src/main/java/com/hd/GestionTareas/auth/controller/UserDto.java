package com.hd.GestionTareas.auth.controller;

public record UserDto(
        String nombres,
        String apellidos,
        String email,
        String rol
) {
}
