package com.hd.GestionTareas.recursoseducativos.controller;

import com.hd.GestionTareas.TipoRecurso;
import com.hd.GestionTareas.error.ErrorResponse;
import com.hd.GestionTareas.recursoseducativos.service.RecursoEducativoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hd.GestionTareas.googledrive.service.GoogleDriveService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recursos-educativos")
@RequiredArgsConstructor
public class RecursoEducativoController {

    private final RecursoEducativoService service;
    private final GoogleDriveService driveService;

    @PostMapping
    public ResponseEntity<?> createRecursoEducativo(@RequestBody RERequest request, HttpServletRequest httpRequest) {
        try {
            service.createRecursoEducativo(request, httpRequest);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'DOCENTE')")
    @PostMapping("/with-file")
    public ResponseEntity<?> createRecursoEducativoWithFile(
            @RequestPart("request") RERequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest) {
        try {
            service.createRecursoEducativoWithFile(request, file, httpRequest);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error processing file", e.getMessage()));
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

    @PatchMapping("/{id}/with-file")
    public ResponseEntity<?> updateRecursoEducativoWithFile(
            @PathVariable Long id,
            @RequestPart("request") REUpdateRequest request,
            @RequestPart("file") MultipartFile file) {
        try {
            service.updateRecursoEducativoWithFile(id, request, file);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad request", e.getMessage()));
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error processing file", e.getMessage()));
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

    @GetMapping("/filtrar")
    public ResponseEntity<List<RESummaryResponse>> filtrarRecursosEducativos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) TipoRecurso tipo,
            @RequestParam(required = false) Long cursoId,
            @RequestParam(required = false) Long creadorId,
            @RequestParam(required = false) Long categoriaId
    ) {
        return ResponseEntity.ok(service.filtrarRecursosEducativos(titulo, tipo, cursoId, creadorId, categoriaId));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RESummaryResponse>> buscarPorTitulo(@RequestParam String titulo){
        return ResponseEntity.ok(service.buscarPorTitulo(titulo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecursoEducativo(@PathVariable Long id) {
        try {
            service.deleteRecursoEducativo(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error deleting file", e.getMessage()));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = driveService.uploadFile(file);
            return ResponseEntity.ok(fileId);
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error uploading file", e.getMessage()));
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileId) {
        try {
            byte[] fileContent = driveService.downloadFile(fileId);
            ByteArrayResource resource = new ByteArrayResource(fileContent);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"")
                    .body(resource);
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error downloading file", e.getMessage()));
        }
    }

    @GetMapping("/url/{fileId}")
    public ResponseEntity<?> getFileUrl(@PathVariable String fileId) {
        try {
            String url = driveService.getFileUrl(fileId);
            return ResponseEntity.ok(url);
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error getting file URL", e.getMessage()));
        }
    }

    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileId) {
        try {
            driveService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (IOException | GeneralSecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error deleting file", e.getMessage()));
        }
    }
}
