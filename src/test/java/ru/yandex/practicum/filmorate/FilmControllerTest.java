package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {

    static FilmController filmController;

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createFailDate() {
        Film film = Film.builder()
                .id(1)
                .name("Кинч 1")
                .description("Описание кинча")
                .releaseDate(LocalDate.of(555, 2, 3))
                .build();
        ValidationException thrown = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertTrue(thrown.getMessage().contains("Дата не валидна"));
    }

    @Test
    void putWithoutId() {
        Film film = Film.builder()
                .name("Фильм 1")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2022, 2, 3))
                .build();
        ValidationException thrown = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.put(film));
        assertTrue(thrown.getMessage().contains("film без id"));
    }

    @Test
    void putUnexisted() {
        Film film = Film.builder()
                .id(1052)
                .name("Фильм 1")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2022, 2, 3))
                .build();
        ValidationException thrown = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.put(film));
        assertTrue(thrown.getMessage().contains("film не найден"));
    }

    @Test
    void createBlankName() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2022, 2, 3))
                .duration(120)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("не должно быть пустым")));
    }

    @Test
    void createDescriptionOver200() {
        Film film = Film.builder()
                .id(1)
                .name("Фильм 1")
                .description("Описание фильма 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890")
                .releaseDate(LocalDate.of(2022, 2, 3))
                .duration(120)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("Описание должно быть меньше 200 символов")));
    }

    @Test
    void createDurationNegative() {
        Film film = Film.builder()
                .id(1)
                .name("Фильм")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2022, 2, 3))
                .duration(-120)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.forEach(i-> System.out.println(i.getMessage()));
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("должно быть больше 0")));
    }
}
