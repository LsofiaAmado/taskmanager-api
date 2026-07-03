package com.empresa.taskmanager.service;

import com.empresa.taskmanager.exception.EmailAlreadyExistsException;
import com.empresa.taskmanager.repository.UserRepository;
import com.empresa.taskmanager.dto.auth.UserRequest;
import com.empresa.taskmanager.dto.auth.UserResponse;
import com.empresa.taskmanager.model.Role;
import com.empresa.taskmanager.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_success(){

        UserRequest request = new UserRequest();
        request.setNombre("Sofia");
        request.setEmail("sofia@gmail.com");
        request.setPassword("1234567");

        User savedUser = User.builder()
                .id(1L)
                .nombre("Sofia")
                .email("sofia@gmail.com")
                .password("passwordEncriptado")
                .role(Role.USER)
                .build();

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("passwordEncriptado");

        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Sofia", response.getNombre());
        assertEquals("sofia@gmail.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());

        verify(repository).findByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getPassword());
        verify(repository).save(any(User.class));

    }

    @Test
    void  register_emailAlreadyExists() {

        UserRequest request = new UserRequest();

        request.setNombre("Sofia");
        request.setEmail("sofia@gmail.com");
        request.setPassword("1234567");

        User existingUser = User.builder()
                .id(1L)
                .nombre("Sofia")
                .email("sofia@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(request));

        verify(repository).findByEmail(request.getEmail());

    }

}
