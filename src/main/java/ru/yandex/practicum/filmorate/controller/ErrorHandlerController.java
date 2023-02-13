package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.exeption.*;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidationException(final ValidationException e) {
        log.warn("error 400 {}", e.getMessage());
        return Map.of("400 Validation error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final ObjectNotFoundException e) {
        log.warn("error 404 {}", e.getMessage());
        return Map.of("404 Object not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerThrowable(final Throwable e) {
        log.warn("error 500 {}", e.getMessage());
        return Map.of("500 Throwable error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerThrowable(final MethodArgumentNotValidException e) {
        log.warn("error 400 {}", e.getMessage());
        return Map.of("400 Throwable error", e.getMessage());
    }
  }