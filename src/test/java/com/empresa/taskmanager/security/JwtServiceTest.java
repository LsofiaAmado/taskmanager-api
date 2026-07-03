package com.empresa.taskmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_seccess() {

        UserDetails userDetails = User.withUsername("sofia@gmail.com")
                .password("1234567")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);

    }

    @Test
    void extractUsername_success() {

        UserDetails userDetails = User.withUsername("sofia@gmail.com")
                .password("1234567")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertEquals("sofia@gmail.com", username);

    }

    @Test
    void isTokenValid_success() {

        UserDetails userDetails = User.withUsername("sofia@gmail.com")
                .password("1234567")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);

    }

    @Test
    void isTokenValid_false() {

        UserDetails sofia = User.withUsername("sofia@gmail.com")
                .password("1234567")
                .authorities("USER")
                .build();

        UserDetails admin = User.withUsername("admin@gmail.com")
                .password("1234567")
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(sofia);

        boolean valid = jwtService.isTokenValid(token, admin);

        assertFalse(valid);

    }

}











