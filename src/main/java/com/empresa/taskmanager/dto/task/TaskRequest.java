package com.empresa.taskmanager.dto.task;

import com.empresa.taskmanager.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotNull
    private Priority priority;

    @NotNull
    private LocalDate fechaLimite;

}
