package com.hd.GestionTareas.auth.controller;

import com.hd.GestionTareas.auth.service.AuthService;
import com.hd.GestionTareas.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request, HttpServletResponse response) {
        try{
            service.authenticate(request, response);
            return ResponseEntity.noContent().build();
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad request", e.getMessage()));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Not found", e.getMessage()));
        }catch (LockedException e){
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(new ErrorResponse(423, "Locked", e.getMessage()));
        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad credentials", e.getMessage()));
        }

    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        try {
            var user = service.getAuthenticatedUser(request);
            return ResponseEntity.ok(
                    Map.of(
                            "id", user.getId(),
                            "nombres", user.getNombres(),
                            "email", user.getEmail(),
                            "rol", user.getRol()
                    )
            );
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "No autorizado", e.getMessage()));
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest request) {
        service.registerUser(request);
        return ResponseEntity.noContent().build();
    }
}
