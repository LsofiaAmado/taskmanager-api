package com.empresa.taskmanager.integration;

import com.empresa.taskmanager.dto.auth.LoginRequest;
import com.empresa.taskmanager.dto.auth.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //@Autowired
    //private ObjectMapper objectMapper;

    @Test
    void register_success() throws Exception {

        UserRequest request = new UserRequest();

        request.setNombre("Usuario Test");
        request.setEmail("test" + System.currentTimeMillis() + "@gmail.com");
        request.setPassword("12345678");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated());

    }

    @Test
    void register_emailAlreadyExists() throws Exception {

        String email = "duplicado" + System.currentTimeMillis() + "@gmail.com";

        UserRequest request = new UserRequest();
        request.setNombre("Usuario Test");
        request.setEmail(email);
        request.setPassword("12345678");

        // Primer registro
        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        // Segundo registro con el mismo email
        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict());

    }

    @Test
    void login_success() throws Exception {

        String email = "login" + System.currentTimeMillis() + "@gmail.com";

        UserRequest userRequest = new UserRequest();
        userRequest.setNombre("Usuario Login");
        userRequest.setEmail(email);
        userRequest.setPassword("12345678");

        // Registrar usuario
        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequest))
                )
                .andExpect(status().isCreated());

        // Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("12345678");

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk());

    }

}
