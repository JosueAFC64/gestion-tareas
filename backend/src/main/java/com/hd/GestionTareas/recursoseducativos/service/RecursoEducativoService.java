package com.hd.GestionTareas.recursoseducativos.service;

import com.hd.GestionTareas.TipoRecurso;
import com.hd.GestionTareas.auth.service.JwtService;
import com.hd.GestionTareas.categoria.repository.Categoria;
import com.hd.GestionTareas.categoria.repository.CategoriaRepository;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hd.GestionTareas.googledrive.service.GoogleDriveService;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoEducativoService {

    private final RecursoEducativoRepository repository;
    private final CursoRepository cursoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;
    private final GoogleDriveService driveService;
    private final JwtService jwtService;

    /**
     * Crea un nuevo recurso educativo para un curso
     *
     * @param request datos necesarios para crear un libro
     */
    public void createRecursoEducativo(RERequest request, HttpServletRequest httpRequest) {
        String token = extractTokenFromCookies(httpRequest);
        if (token == null) {
            throw new SecurityException("Token no encontrado");
        }
        Long userId = jwtService.extractUserId(token);
        User creador = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        RecursosEducativo recursoEducativoNuevo = RecursosEducativo.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .tipo(request.tipo())
                .url(request.url())
                .creador(creador)
                .curso(curso)
                .categoria(categoria)
                .build();

        repository.save(recursoEducativoNuevo);
    }

    /**
     * Crea un nuevo recurso educativo con archivo para un curso
     *
     * @param request datos necesarios para crear un recurso
     * @param file    archivo a subir
     */
    @Transactional
    public void createRecursoEducativoWithFile(RERequest request, MultipartFile file, HttpServletRequest httpRequest) throws IOException, GeneralSecurityException {
        String token = extractTokenFromCookies(httpRequest);
        if (token == null) {
            throw new SecurityException("Token no encontrado");
        }
        Long userId = jwtService.extractUserId(token);
        User creador = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        // Subir archivo a Google Drive
        String fileId = driveService.uploadFile(file);
        String fileUrl = driveService.getFileUrl(fileId);

        RecursosEducativo recursoEducativoNuevo = RecursosEducativo.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .tipo(request.tipo())
                .url(fileUrl)
                .googleDriveFileId(fileId)
                .creador(creador)
                .curso(curso)
                .categoria(categoria)
                .build();

        repository.save(recursoEducativoNuevo);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("USER_SESSION".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un recurso educativo
     *
     * @param id      El id del recurso educativo a actualizar
     * @param request Datos disponibles para actualizar
     */
    public void updateRecursoEducativo(Long id, REUpdateRequest request) {
        if (id == null || id <= 0) {
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
     * Actualiza un recurso educativo y su archivo
     *
     * @param id      El id del recurso educativo a actualizar
     * @param request Datos disponibles para actualizar
     * @param file    Nuevo archivo a subir
     */
    public void updateRecursoEducativoWithFile(Long id, REUpdateRequest request, MultipartFile file) throws IOException, GeneralSecurityException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id del recurso no es válido");
        }

        RecursosEducativo recursoEducativo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado"));

        if (request.titulo() == null || request.descripcion() == null || request.tipo() == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }

        // Si hay un archivo anterior, eliminarlo
        if (recursoEducativo.getGoogleDriveFileId() != null) {
            driveService.deleteFile(recursoEducativo.getGoogleDriveFileId());
        }

        // Subir nuevo archivo
        String fileId = driveService.uploadFile(file);
        String fileUrl = driveService.getFileUrl(fileId);

        recursoEducativo.setTitulo(request.titulo());
        recursoEducativo.setDescripcion(request.descripcion());
        recursoEducativo.setTipo(request.tipo());
        recursoEducativo.setUrl(fileUrl);
        recursoEducativo.setGoogleDriveFileId(fileId);

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
                .stream().map(re -> new RESummaryResponse(
                        re.getId(),
                        re.getTitulo(),
                        re.getDescripcion(),
                        re.getTipo(),
                        re.getUrl(),
                          re.getCreador().getNombres(),
                        re.getCurso().getNombre(),
                        re.getCategoria().getNombre(),
                        re.getFechaCreacion()
                )).toList();
    }

    /**
     * Elimina un recurso educativo y su archivo asociado
     *
     * @param id Id del recurso educativo a eliminar
     */
    @Transactional
    public void deleteRecursoEducativo(Long id) throws IOException, GeneralSecurityException {
        RecursosEducativo recursoEducativo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado"));

        // Si tiene un archivo en Google Drive, eliminarlo
        if (recursoEducativo.getGoogleDriveFileId() != null) {
            driveService.deleteFile(recursoEducativo.getGoogleDriveFileId());
        }

        repository.delete(recursoEducativo);
    }

    public List<RESummaryResponse> buscarPorTitulo(String titulo) {
        return repository.filtrarResumen(titulo, null, null, null, null);
    }

    public List<RESummaryResponse> filtrarRecursosEducativos(String titulo, TipoRecurso tipo, Long cursoId, Long creadorId, Long categoriaId) {
        return repository.filtrarResumen(titulo, tipo, cursoId, creadorId, categoriaId);
    }

}
