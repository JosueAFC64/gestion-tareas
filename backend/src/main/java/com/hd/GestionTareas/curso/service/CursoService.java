package com.hd.GestionTareas.curso.service;

import com.hd.GestionTareas.curso.controller.CursoRequest;
import com.hd.GestionTareas.curso.controller.CursoResponse;
import com.hd.GestionTareas.curso.repository.Curso;
import com.hd.GestionTareas.curso.repository.CursoRepository;
import com.hd.GestionTareas.user.repository.User;
import com.hd.GestionTareas.user.repository.UserRepository;
import com.hd.GestionTareas.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public void createCurso(CursoRequest request) {
        // Validaciones básicas
        if (request.codigo().isEmpty()) {
            throw new IllegalArgumentException("El código del curso no puede estar vacío");
        }
        if (repository.existsByCodigo(request.codigo())) {
            throw new IllegalArgumentException("El código del curso ya existe");
        }
        if (request.nombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacío");
        }
        if (repository.existsByNombre(request.nombre())) {
            throw new IllegalArgumentException("El nombre del curso ya existe");
        }

        // Buscar docentes (validando que existan y sean realmente docentes)
        Set<User> docentes = userRepository.findAllById(request.docentesAsignadosIds())
                .stream()
                .filter(user -> Objects.equals(user.getRol(), "DOCENTE"))
                .collect(Collectors.toSet());

        if (docentes.size() != request.docentesAsignadosIds().size()) {
            throw new IllegalArgumentException("Algunos IDs no corresponden a docentes válidos");
        }

        // Crear el curso
        Curso curso = Curso.builder()
                .codigo(request.codigo())
                .nombre(request.nombre())
                .build();

        // Asignar docentes al curso
        docentes.forEach(curso::agregarDocente);

        // Guardar el curso
        repository.save(curso);
    }

    /**
     * Obtiene todos los cursos asignados al docente actual
     */
    @Transactional
    public List<CursoResponse> getCursosByDocente(HttpServletRequest request) {
        Long userInSessionId = userService.getUserInSessionData(request).id();

        User docente = userRepository.findById(userInSessionId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Set<Curso> cursoAsignados = docente.getCursosAsignados();

        return cursoAsignados
                .stream()
                .map(cursoAsignado -> new CursoResponse(
                        cursoAsignado.getId(),
                        cursoAsignado.getCodigo(),
                        cursoAsignado.getNombre(),
                        docente.getNombres() + " " + docente.getApellidos()

                )).toList();
    }

}
