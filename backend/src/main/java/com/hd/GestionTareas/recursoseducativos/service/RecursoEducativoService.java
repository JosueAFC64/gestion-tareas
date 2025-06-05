package com.hd.GestionTareas.recursoseducativos.service;

import com.hd.GestionTareas.curso.repository.Curso;
import com.hd.GestionTareas.curso.repository.CursoRepository;
import com.hd.GestionTareas.recursoseducativos.controller.RERequest;
import com.hd.GestionTareas.recursoseducativos.controller.RESummaryResponse;
import com.hd.GestionTareas.recursoseducativos.controller.REUpdateRequest;
import com.hd.GestionTareas.recursoseducativos.repository.RecursoEducativoRepository;
import com.hd.GestionTareas.recursoseducativos.repository.RecursosEducativo;
import com.hd.GestionTareas.user.repository.User;
import com.hd.GestionTareas.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoEducativoService {

    private final RecursoEducativoRepository repository;
    private final CursoRepository cursoRepository;
    private final UserRepository userRepository;

    /**
     * Crea un nuevo recurso educativo para un curso
     *
     * @param request datos necesarios para crear un libro
     */
    public void createRecursoEducativo(RERequest request) {
        User creador = userRepository.findById(request.creadorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        RecursosEducativo recursoEducativoNuevo = RecursosEducativo.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .tipo(request.tipo())
                .url(request.url())
                .creador(creador)
                .curso(curso)
                .build();

        repository.save(recursoEducativoNuevo);
    }

    /**
     * Actualiza los datos de un recurso educativo
     *
     * @param id El id del recurso educativo a actualizar
     * @param request Datos disponibles para actualizar
     */
    public void updateRecursoEducativo(Long id, REUpdateRequest request) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("Id del recurso no es válido");
        }

        RecursosEducativo recursoEducativo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado"));

        if (request.titulo() == null || request.descripcion() == null || request.tipo() == null || request.url() == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }

        recursoEducativo.setTitulo(request.titulo());
        recursoEducativo.setDescripcion(request.descripcion());
        recursoEducativo.setTipo(request.tipo());
        recursoEducativo.setUrl(request.url());

        repository.save(recursoEducativo);
    }

    /**
     * Obtiene todos los recursos educativos de un curso específico
     *
     * @param cursoId Id del curso a obtener sus recursos educativos
     * @return Una lista de todos los recursos educativos pertenecientes al curso
     */
    @Transactional(readOnly = true)
    public List<RESummaryResponse> getRecursosEducativosByCurso(Long cursoId) {
        if (cursoId == null || cursoId <= 0) {
            throw new IllegalArgumentException("Id del curso no es válido");
        }

        // Verificar que el curso existe
        cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        List<RecursosEducativo> recursosEducativo = repository.findByCurso_Id(cursoId);

        return recursosEducativo
                .stream()
                .map(recursoEducativo -> new RESummaryResponse(
                        recursoEducativo.getId(),
                        recursoEducativo.getTitulo()
                )).toList();
    }
}
