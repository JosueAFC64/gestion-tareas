package com.hd.GestionTareas.recursoseducativos.controller;

import com.hd.GestionTareas.error.ErrorResponse;
import com.hd.GestionTareas.recursoseducativos.service.RecursoEducativoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recursos-educativos")
@RequiredArgsConstructor
public class RecursoEducativoController {

    private final RecursoEducativoService service;

    @PostMapping
    public ResponseEntity<?> createRecursoEducativo(@RequestBody RERequest request){
        try {
            service.createRecursoEducativo(request);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRecursoEducativo(@PathVariable Long id, @RequestBody REUpdateRequest request){
        try{
            service.updateRecursoEducativo(id, request);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad request", e.getMessage()));
        }
    }


    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<?> getRecursosEducativosByCurso(@PathVariable Long cursoId) {
        try {
            List<RESummaryResponse> recursos = service.getRecursosEducativosByCurso(cursoId);
            return ResponseEntity.ok(recursos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad request", e.getMessage()));
        }
    }
}
