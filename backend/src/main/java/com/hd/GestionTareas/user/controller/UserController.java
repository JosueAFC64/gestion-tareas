package com.hd.GestionTareas.user.controller;

import com.hd.GestionTareas.auth.controller.UserDto;
import com.hd.GestionTareas.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/data")
    public ResponseEntity<UserDto> getUserData(HttpServletRequest request){
        UserDto user = service.getUserData(request);
        return ResponseEntity.ok(user);
    }

}
