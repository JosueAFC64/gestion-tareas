package com.hd.GestionTareas.user.controller;

import com.hd.GestionTareas.auth.controller.RegisterRequest;
import com.hd.GestionTareas.error.ErrorResponse;
import com.hd.GestionTareas.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/register-user")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest request) {
        service.registerUser(request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/session/user-data")
    public ResponseEntity<?> getUserInSessionData(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(service.getUserInSessionData(request));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not Found", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/docentes")
    public ResponseEntity<List<DocenteResponse>> getDocentes(){
        return ResponseEntity.ok(service.getDocentes());
    }

}
