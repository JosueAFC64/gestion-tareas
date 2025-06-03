package com.hd.GestionTareas.user.controller;

public record UserDto(
        String nombres,
        String apellidos,
        String email,
        String rol
) {
}
