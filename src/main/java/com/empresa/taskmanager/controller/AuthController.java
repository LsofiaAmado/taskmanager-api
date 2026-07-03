package com.empresa.taskmanager.controller;

import com.empresa.taskmanager.dto.auth.UserRequest;
import com.empresa.taskmanager.dto.auth.UserResponse;
import com.empresa.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.empresa.taskmanager.dto.auth.LoginRequest;
import com.empresa.taskmanager.dto.auth.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service){
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRequest request){
        return service.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                service.login(request)
        );
    }

}
