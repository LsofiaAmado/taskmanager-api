package com.empresa.taskmanager.dto.task;

import com.empresa.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    private TaskStatus status;

}
