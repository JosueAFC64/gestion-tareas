package com.hd.GestionTareas.categoria.controller;

import com.hd.GestionTareas.categoria.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CategoriaRequest request){
        service.save(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> readAll(){
        return ResponseEntity.ok(service.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody CategoriaRequest request, @PathVariable Long id){
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
