package com.example.shcool_hogwarts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> clazz, long id) {
        super("%s not found with ID: [%s]".formatted(clazz.getSimpleName(), id));
    }
}

