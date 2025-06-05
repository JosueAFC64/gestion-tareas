package com.hd.GestionTareas.curso.controller;

import com.hd.GestionTareas.curso.service.CursoService;
import com.hd.GestionTareas.error.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService service;

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> createCurso(@RequestBody CursoRequest request) {
        try {
            service.createCurso(request);
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage()));
        }
    }

    @GetMapping("/docente/cursos")
    public ResponseEntity<?> getCursosByDocente(HttpServletRequest request) {
        try {
            List<CursoResponse> cursos = service.getCursosByDocente(request);
            return ResponseEntity.ok(cursos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        }
    }

}
