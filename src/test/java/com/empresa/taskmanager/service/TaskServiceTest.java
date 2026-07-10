package com.empresa.taskmanager.service;

import com.empresa.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.empresa.taskmanager.exception.TaskNotFoundException;
import com.empresa.taskmanager.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.empresa.taskmanager.dto.task.TaskRequest;
import com.empresa.taskmanager.dto.task.TaskResponse;
import com.empresa.taskmanager.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_success() {

        User user = User.builder()
                .id(1L)
                .nombre("Laura")
                .email("laura@gmail.com")
                .build();

        TaskRequest request = new TaskRequest();

        request.setTitulo("Estudiar Spring");
        request.setDescripcion("JWT");
        request.setPriority(Priority.HIGH);
        request.setFechaLimite(LocalDate.now());

        Task savedTask = Task.builder()
                .id(1L)
                .titulo("Estudiar Spring")
                .descripcion("JWT")
                .priority(Priority.HIGH)
                .status(TaskStatus.PENDING)
                .fechaLimite(LocalDate.now())
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals("Estudiar Spring", response.getTitulo());
        assertEquals(TaskStatus.PENDING, response.getStatus());

        verify(currentUserService).getCurrentUser();
        verify(repository).save(any(Task.class));

    }

    @Test
    void getTaskById_success() {

        User user = User.builder()
                .id(1L)
                .nombre("Sofia")
                .email("sofia@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Estudiar Spring")
                .descripcion("JWT")
                .priority(Priority.HIGH)
                .status(TaskStatus.PENDING)
                .fechaLimite(LocalDate.now())
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUser(1L, user)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals("Estudiar Spring", response.getTitulo());

        verify(repository).findByIdAndUser(1L, user);

    }

    @Test
    void getTaskById_notFound() {

        User user = User.builder()
                .id(1L)
                .email("sofia@gmail.com")
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));

        verify(repository).findByIdAndUser(1L, user);

    }

    @Test
    void updateTask_success() {

        User user = User.builder()
                .id(1L)
                .email("sofia@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Título antiguo")
                .descripcion("Descripción antigua")
                .priority(Priority.LOW)
                .status(TaskStatus.PENDING)
                .fechaLimite(LocalDate.now())
                .user(user)
                .build();

        TaskRequest request = new TaskRequest();

        request.setTitulo("Nuevo");
        request.setDescripcion("Nueva descripcion");
        request.setPriority(Priority.HIGH);
        request.setFechaLimite(LocalDate.now().plusDays(7));

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUser(1L, user)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateTask(1L, request);

        assertEquals("Nuevo", response.getTitulo());
        assertEquals(Priority.HIGH, response.getPriority());

        verify(repository).save(task);

    }

    @Test
    void deleteTask_success() {

        User user = User.builder()
                .id(1L)
                .email("sofia@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring")
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUser(1L, user)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(repository).delete(task);

    }

    @Test
    void getAllTasks_success() {

        User user = User.builder()
                .id(1L)
                .email("sofia@gmail.com")
                .build();

        Task task1 = Task.builder()
                .id(1L)
                .titulo("Spring")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .user(user)
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .titulo("PostgreSQL")
                .status(TaskStatus.COMPLETED)
                .priority(Priority.MEDIUM)
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByUser(user)).thenReturn(List.of(task1, task2));

        List<TaskResponse> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Spring", result.get(0).getTitulo());

        verify(repository).findByUser(user);

    }

    @Test
    void updateTaskStatus_success() {

        User user = User.builder()
                .id(1L)
                .email("laura@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .user(user)
                .build();

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();

        request.setStatus(TaskStatus.COMPLETED);

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByIdAndUser(1L, user)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateTaskStatus(1L, request);

        assertEquals(TaskStatus.COMPLETED, response.getStatus());

        verify(repository).save(task);
    }

    @Test
    void searchTasks_success() {

        User user = User.builder()
                .id(1L)
                .email("laura@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring Security")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.searchTasks(user, "Spring")).thenReturn(List.of(task));

        List<TaskResponse> result = taskService.searchTasks("Spring");

        assertEquals(1, result.size());
        assertEquals("Spring Security", result.get(0).getTitulo());

    }

    @Test
    void getTasksByStatus_success() {

        User user = User.builder()
                .id(1L)
                .email("laura@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByUserAndStatus(user, TaskStatus.PENDING))
                .thenReturn(List.of(task));

        List<TaskResponse> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(TaskStatus.PENDING, result.get(0).getStatus());

        verify(repository).findByUserAndStatus(user, TaskStatus.PENDING);
    }

    @Test
    void getTasksByPriority_success() {

        User user = User.builder()
                .id(1L)
                .email("laura@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring")
                .priority(Priority.HIGH)
                .status(TaskStatus.PENDING)
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(repository.findByUserAndPriority(user, Priority.HIGH))
                .thenReturn(List.of(task));

        List<TaskResponse> result = taskService.getTasksByPriority(Priority.HIGH);

        assertEquals(1, result.size());
        assertEquals(Priority.HIGH, result.get(0).getPriority());

        verify(repository).findByUserAndPriority(user, Priority.HIGH);
    }

    @Test
    void filterTasks_success() {

        User user = User.builder()
                .id(1L)
                .email("laura@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .user(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(repository.findByUserAndStatusAndPriority(
                user,
                TaskStatus.PENDING,
                Priority.HIGH))
                .thenReturn(List.of(task));

        List<TaskResponse> result =
                taskService.filterTasks(TaskStatus.PENDING, Priority.HIGH);

        assertEquals(1, result.size());

        verify(repository)
                .findByUserAndStatusAndPriority(
                        user,
                        TaskStatus.PENDING,
                        Priority.HIGH);
    }

    @Test
    void getTasks_success() {

        User user = User.builder()
                .id(1L)
                .email("laura@gmail.com")
                .build();

        Task task = Task.builder()
                .id(1L)
                .titulo("Spring")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .user(user)
                .build();

        Page<Task> page = new PageImpl<>(List.of(task));

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(repository.findByUser(
                eq(user),
                any(Pageable.class)))
                .thenReturn(page);

        Page<TaskResponse> result =
                taskService.getTasks(0, 10, "titulo", "asc");

        assertEquals(1, result.getContent().size());

        verify(repository)
                .findByUser(eq(user), any(Pageable.class));
    }

}
