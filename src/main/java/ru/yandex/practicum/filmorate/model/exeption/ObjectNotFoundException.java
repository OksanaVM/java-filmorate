package ru.yandex.practicum.filmorate.model.exeption;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}