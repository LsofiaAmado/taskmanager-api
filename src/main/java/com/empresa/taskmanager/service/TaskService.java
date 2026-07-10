package com.empresa.taskmanager.service;

import com.empresa.taskmanager.dto.task.TaskRequest;
import com.empresa.taskmanager.dto.task.TaskResponse;
import com.empresa.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.empresa.taskmanager.exception.TaskNotFoundException;
import com.empresa.taskmanager.model.Priority;
import com.empresa.taskmanager.model.Task;
import com.empresa.taskmanager.model.TaskStatus;
import com.empresa.taskmanager.model.User;
import com.empresa.taskmanager.repository.TaskRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository repository;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public TaskService(TaskRepository repository, UserService userService, CurrentUserService currentUserService) {

        this.repository = repository;
        this.userService = userService;
        this.currentUserService = currentUserService;

    }

    // Crear tarea
    public TaskResponse createTask(TaskRequest request) {

        User user = currentUserService.getCurrentUser();
        log.info("Usuario {} está creando una nueva tarea ", user.getEmail());

        Task task = Task.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .priority(request.getPriority())
                .fechaLimite(request.getFechaLimite())
                .status(TaskStatus.PENDING)
                .user(user)
                .build();
        Task savedTask = repository.save(task);
        log.info("Tarea {} creada correctamente con ID {}", savedTask.getTitulo(), savedTask.getId());

        return mapToResponse(savedTask);
    }

    // Obtener tareas
    public List<TaskResponse> getAllTasks() {

        User user = currentUserService.getCurrentUser();
        log.info("Consultando todas las tareas del usuario: {}", user.getEmail());

        return repository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    // Paginación
    public Page<TaskResponse> getTasks(int page, int size, String sortBy, String direction) {

        User user = currentUserService.getCurrentUser();

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<Task> tasks =  repository.findByUser(user, PageRequest.of(page, size, sort));
        return tasks.map(this::mapToResponse);

    }

    // Obtener tarea por id
    public TaskResponse getTaskById(Long id) {

        log.info("Consultando tarea con ID {}", id);
        Task task = getTaskByIdAndUser(id);
        return mapToResponse(task);

    }

    // Actualizar tarea
    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task task = getTaskByIdAndUser(id);
        log.info("Actualizando tarea con ID {}", id);

        task.setTitulo(request.getTitulo());
        task.setDescripcion(request.getDescripcion());
        task.setPriority(request.getPriority());
        task.setFechaLimite(request.getFechaLimite());

        Task updatedTask = repository.save(task);
        log.info("Tarea {} actualizada correctamente", id);

        return mapToResponse(updatedTask);
    }

    // Eliminar tarea
    public void deleteTask(Long id) {

        Task task = getTaskByIdAndUser(id);
        log.warn("Eliminando tarea con ID {}", id);
        repository.delete(task);

    }

   // Mapper
    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .titulo(task.getTitulo())
                .descripcion(task.getDescripcion())
                .status(task.getStatus())
                .priority(task.getPriority())
                .fechaLimite(task.getFechaLimite())
                .build();
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {

        User user = currentUserService.getCurrentUser();
        return repository.findByUserAndStatus(user, status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByPriority(Priority priority){

        User user = currentUserService.getCurrentUser();
        return repository.findByUserAndPriority(user, priority)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> filterTasks(TaskStatus status, Priority priority){

        User user = currentUserService.getCurrentUser();
        return repository.findByUserAndStatusAndPriority(user, status, priority)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request){

        Task task = getTaskByIdAndUser(id);
        task.setStatus(request.getStatus());
        log.info("Actualizando estado de la tarea {} a {}", id, request.getStatus());

        Task updatedTask = repository.save(task);

        return mapToResponse(updatedTask);

    }

    public List<TaskResponse> searchTasks(String keyword){

        User user = currentUserService.getCurrentUser();
        log.info("Buscando tareas con la palabra clave: {}", keyword);

        return repository.searchTasks(user, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    private Task getTaskByIdAndUser(Long id){

        User user = currentUserService.getCurrentUser();
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new TaskNotFoundException(id));

    }

}
