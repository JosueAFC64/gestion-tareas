package com.hd.GestionTareas.user.service;

import com.hd.GestionTareas.auth.controller.UserDto;
import com.hd.GestionTareas.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;

    public UserDto getUserData(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("USER_SESSION")){
                    Claims claims = jwtService.extractAllClaims(cookie.getValue());
                    return new UserDto(
                            claims.get("nombres", String.class),
                            claims.get("apellidos", String.class),
                            claims.getSubject(),
                            claims.get("rol", String.class)
                    );
                }
            }
        }
        return null;
    }

}
