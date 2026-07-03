package com.empresa.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("La tarea con id " + id + " no fue encontrada");
    }

}
