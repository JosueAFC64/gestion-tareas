package com.hd.GestionTareas.auth.service;

import com.hd.GestionTareas.auth.controller.AuthRequest;
import com.hd.GestionTareas.auth.controller.RegisterRequest;
import com.hd.GestionTareas.auth.repository.Token;
import com.hd.GestionTareas.auth.repository.TokenRepository;
import com.hd.GestionTareas.user.repository.User;
import com.hd.GestionTareas.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Autentica al usuario, genera un token JWT y lo agrega en una Cookie
     *
     * @param request Datos necesarios para la autenticación (email y contraseña)
     * @param response Objeto HttpServletResponse para agregar cookies
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Transactional
    public void authenticate(AuthRequest request, HttpServletResponse response) {

        if(request.email() == null || request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Email o contraseña inválidos");
        }

        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> {
                    loginAttemptService.loginFailed(request.email());
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        if(loginAttemptService.isBlocked(user.getEmail())){
            throw new LockedException("La cuenta está temporalmente bloqueada por demasiados intentos fallidos, intente de nuevo en 1 minutos");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e){
            loginAttemptService.loginFailed(request.email());
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        loginAttemptService.loginSucceeded(request.email());

        final String token = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, token);

        var cookie = new jakarta.servlet.http.Cookie("USER_SESSION", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpiration)/1000);

        response.addCookie(cookie);
    }

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

    /**
     * Guarda el token del usuario en la Base de Datos
     *
     * @param user Objeto {@link com.hd.GestionTareas.user.repository.User} (solo almacena el ID del usuario)
     * @param jwtToken El JWT generado del usuario
     */
    private void saveUserToken(User user, String jwtToken){
        final Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();

        tokenRepository.save(token);
    }

    /**
     * Invalida todos los token de un usuario
     *
     * @param user Objeto {@link com.hd.GestionTareas.user.repository.User} del usuario a invalidar sus tokens
     * */
    private void revokeAllUserTokens(final User user){
        final List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(!validUserTokens.isEmpty()){
            validUserTokens.forEach(token -> {
                token.setIsRevoked(true);
                token.setIsExpired(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = extractTokenFromCookies(request);
        if (token == null) throw new SecurityException("Token no encontrado");

        Long userId = jwtService.extractUserId(token);

        return repository.findById(userId)
                .orElseThrow(() -> new SecurityException("Usuario no encontrado"));
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
}
