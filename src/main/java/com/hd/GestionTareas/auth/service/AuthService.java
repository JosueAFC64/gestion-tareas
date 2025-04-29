package com.hd.GestionTareas.auth.service;

import com.hd.GestionTareas.auth.controller.AuthRequest;
import com.hd.GestionTareas.auth.controller.RegisterRequest;
import com.hd.GestionTareas.auth.repository.Token;
import com.hd.GestionTareas.auth.repository.TokenRepository;
import com.hd.GestionTareas.user.repository.User;
import com.hd.GestionTareas.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public void authenticate(AuthRequest request, HttpServletResponse response){
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        final String token = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user,token);

        var cookie = new jakarta.servlet.http.Cookie("USER_SESSION", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpiration)/1000);

        response.addCookie(cookie);
    }

    public void register(RegisterRequest request){
        final User user = User.builder()
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol())
                .build();

        repository.save(user);
    }

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

}
