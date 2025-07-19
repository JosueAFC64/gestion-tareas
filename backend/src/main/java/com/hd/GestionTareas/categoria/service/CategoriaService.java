package com.hd.GestionTareas.categoria.service;

import com.hd.GestionTareas.categoria.controller.CategoriaRequest;
import com.hd.GestionTareas.categoria.controller.CategoriaResponse;
import com.hd.GestionTareas.categoria.repository.Categoria;
import com.hd.GestionTareas.categoria.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    public void save(CategoriaRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.nombre() == null || request.nombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        if (repository.existsByNombre(request.nombre())){
            throw new IllegalArgumentException("La categoría ya existe");
        }

        Categoria newCategoria = Categoria.builder()
                .nombre(request.nombre())
                .build();
        repository.save(newCategoria);
    }

    public List<CategoriaResponse> readAll(){
        return repository.findAll()
                .stream()
                .map(categoria -> new CategoriaResponse(
                        categoria.getId(),
                        categoria.getNombre()
                )).toList();
    }

    public void update(Long id, CategoriaRequest request) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalido");
        }

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        categoria.setNombre(request.nombre());
        repository.save(categoria);
    }

    public void delete(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalido");
        }

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        repository.delete(categoria);
    }



}
