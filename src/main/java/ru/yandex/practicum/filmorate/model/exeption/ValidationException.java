package ru.yandex.practicum.filmorate.model.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
