package com.empresa.taskmanager.controller;

import com.empresa.taskmanager.dto.auth.LoginRequest;
import com.empresa.taskmanager.dto.auth.UserRequest;
import com.empresa.taskmanager.dto.task.TaskRequest;
import com.empresa.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.empresa.taskmanager.model.Priority;
import com.empresa.taskmanager.model.TaskStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private String token;

    @BeforeEach
    void setup() throws Exception {

        String email = "task" + System.currentTimeMillis() + "@gmail.com";

        UserRequest user = new UserRequest();
        user.setNombre("Laura");
        user.setEmail(email);
        user.setPassword("12345678");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword("12345678");

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = mapper.readTree(response);
        token = json.get("token").asText();
    }

    @Test
    void createTask() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitulo("Aprender Spring");
        request.setDescripcion("JWT");
        request.setPriority(Priority.HIGH);
        request.setFechaLimite(LocalDate.now().plusDays(5));

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTasks() throws Exception {

        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void searchTasks() throws Exception {

        mockMvc.perform(get("/tasks/search")
                        .header("Authorization", "Bearer " + token)
                        .param("keyword", "Spring"))
                .andExpect(status().isOk());
    }

    @Test
    void filterTasks() throws Exception {

        mockMvc.perform(get("/tasks/filter")
                        .header("Authorization", "Bearer " + token)
                        .param("status", "PENDING")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk());
    }

    @Test
    void getTasksByStatus() throws Exception {

        mockMvc.perform(get("/tasks/status/PENDING")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getTasksByPriority() throws Exception {

        mockMvc.perform(get("/tasks/priority/HIGH")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getTasksPage() throws Exception {

        mockMvc.perform(get("/tasks/page")
                        .header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());

    }

    @Test
    void getTaskById() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitulo("Spring");
        request.setDescripcion("JWT");
        request.setPriority(Priority.HIGH);
        request.setFechaLimite(LocalDate.now().plusDays(5));

        String response = mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = mapper.readTree(response);

        Long id = json.get("id").asLong();

        mockMvc.perform(get("/tasks/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }

    @Test
    void updateTask() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitulo("Original");
        request.setDescripcion("Descripcion");
        request.setPriority(Priority.MEDIUM);
        request.setFechaLimite(LocalDate.now().plusDays(3));

        String response = mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = mapper.readTree(response).get("id").asLong();

        request.setTitulo("Actualizado");
        request.setDescripcion("Nueva descripcion");
        request.setPriority(Priority.HIGH);

        mockMvc.perform(put("/tasks/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    void deleteTask() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitulo("Eliminar");
        request.setDescripcion("Eliminar");
        request.setPriority(Priority.LOW);
        request.setFechaLimite(LocalDate.now());

        String response = mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = mapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/tasks/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }

    @Test
    void updateTaskStatus() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitulo("Estado");
        request.setDescripcion("Estado");
        request.setPriority(Priority.HIGH);
        request.setFechaLimite(LocalDate.now());

        String response = mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = mapper.readTree(response).get("id").asLong();

        UpdateTaskStatusRequest status = new UpdateTaskStatusRequest();
        status.setStatus(TaskStatus.COMPLETED);

        mockMvc.perform(patch("/tasks/" + id + "/status")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(status)))
                .andExpect(status().isOk());

    }

}
