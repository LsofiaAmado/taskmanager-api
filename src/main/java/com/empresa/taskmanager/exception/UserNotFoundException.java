package com.empresa.taskmanager.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Usuario con id " + id + " no encontrado");
    }

    public UserNotFoundException(String message){
        super(message);
    }

}
