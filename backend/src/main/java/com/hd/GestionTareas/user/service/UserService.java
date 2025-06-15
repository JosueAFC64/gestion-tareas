package com.hd.GestionTareas.user.service;

import com.hd.GestionTareas.user.controller.DocenteResponse;
import com.hd.GestionTareas.user.controller.UserDataResponse;
import com.hd.GestionTareas.auth.service.JwtService;
import com.hd.GestionTareas.user.repository.User;
import com.hd.GestionTareas.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository repository;

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

    @Transactional(readOnly = true)
    public List<DocenteResponse> getDocentes(){
        return repository.findAll()
                .stream()
                .filter(u -> Objects.equals(u.getRol(), "DOCENTE"))
                .map(user -> new DocenteResponse(user.getId(), user.getNombres()))
                .toList();
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
