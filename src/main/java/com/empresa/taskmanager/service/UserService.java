package com.empresa.taskmanager.service;

import com.empresa.taskmanager.dto.auth.UserRequest;
import com.empresa.taskmanager.dto.auth.UserResponse;
import com.empresa.taskmanager.exception.EmailAlreadyExistsException;
import com.empresa.taskmanager.exception.UserNotFoundException;
import com.empresa.taskmanager.model.Role;
import com.empresa.taskmanager.model.User;
import com.empresa.taskmanager.repository.UserRepository;
import com.empresa.taskmanager.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.empresa.taskmanager.dto.auth.LoginRequest;
import com.empresa.taskmanager.dto.auth.LoginResponse;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponse register(UserRequest request) {

        if(repository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = repository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .nombre(savedUser.getNombre())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()
        );

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    public User getUserByEmail(String email){

        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    }

}
