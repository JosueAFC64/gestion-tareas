package com.hd.GestionTareas.user.service;

import com.hd.GestionTareas.auth.controller.RegisterRequest;
import com.hd.GestionTareas.user.controller.UserDataResponse;
import com.hd.GestionTareas.auth.service.JwtService;
import com.hd.GestionTareas.user.repository.User;
import com.hd.GestionTareas.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra al usuario y lo guarda en la Base de Datos
     *
     * @param request - Datos necesarios para crear un nuevo usuario
     */
    @Transactional
    public void registerUser(RegisterRequest request){

        // Validaciones
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de registro no puede ser nula");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (!request.email().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        if(repository.findByEmail(request.email()).isPresent()){
            throw new IllegalArgumentException("El email ya existe");
        }
        if (!request.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial");
        }

        repository.save(
                User.builder()
                        .nombres(request.nombres())
                        .apellidos(request.apellidos())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .rol(request.rol())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public UserDataResponse getUserInSessionData(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("USER_SESSION")) {
                    Claims claims = jwtService.extractPayload(cookie.getValue());
                    String email = claims.getSubject();

                    User user = repository.findByEmail(email)
                            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

                    return toDataResponse(user);
                }
            }
        }
        return null;
    }

    private UserDataResponse toDataResponse(User user) {
        return new UserDataResponse(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail(),
                user.getRol()
        );
    }

}
