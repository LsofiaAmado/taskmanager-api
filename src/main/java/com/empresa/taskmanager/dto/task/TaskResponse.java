package com.empresa.taskmanager.dto.task;

import com.empresa.taskmanager.model.Priority;
import com.empresa.taskmanager.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private TaskStatus status;
    private Priority priority;
    private LocalDate fechaLimite;

}
