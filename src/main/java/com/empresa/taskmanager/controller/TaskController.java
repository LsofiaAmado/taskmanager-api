package com.empresa.taskmanager.controller;

import com.empresa.taskmanager.dto.task.TaskRequest;
import com.empresa.taskmanager.dto.task.TaskResponse;
import com.empresa.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.empresa.taskmanager.model.Priority;
import com.empresa.taskmanager.model.TaskStatus;
import com.empresa.taskmanager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @Operation(summary = "Crear una nueva tarea")
    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest request) {
        return service.createTask(request);
    }

    @Operation(summary = "Obtener todas las tareas del usuario")
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return service.getAllTasks();
    }

    @Operation(summary = "Obtener una tarea por id")
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @Operation(summary = "Actualizar una tarea")
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return service.updateTask(id, request);
    }

    @Operation(summary = "Eiliminar una tarea")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }

    @GetMapping("/page")
    public Page<TaskResponse> getTasks(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @RequestParam(defaultValue = "asc") String direction) {
        return service.getTasks(page, size, sortBy, direction);
    }

    @GetMapping("/status/{status}")
    public List<TaskResponse> getTasksByStatus(@PathVariable TaskStatus status){
        return service.getTasksByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<TaskResponse> getTasksByPriority(@PathVariable Priority priority){
        return service.getTasksByPriority(priority);
    }

    @GetMapping("/filter")
    public List<TaskResponse> filterTasks(@RequestParam TaskStatus status, @RequestParam Priority priority){
        return service.filterTasks(status, priority);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id, @Valid @RequestBody UpdateTaskStatusRequest request){
        return service.updateTaskStatus(id, request);
    }

    @GetMapping("/search")
    public List<TaskResponse> searchTasks(@RequestParam String keyword){
        return service.searchTasks(keyword);
    }

}
