package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;


import static org.junit.jupiter.api.Assertions.*;
public class FilmsTestController {
    private Film testFilm;
    private FilmController filmController;;

    @BeforeEach
    void before() {
        testFilm = new Film("кино", "описание", "2003-12-12", 100);
        testFilm.setId(1);
        filmController = new FilmController();
    }

    @Test
    public void addFilm() throws Exception {
        assertEquals(filmController.addFilm(testFilm), testFilm);
    }

    @Test
    public void validationBadFilm() {
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.validate(
                new Film("name", "описание", "1895-12-27", 100)));
        assertEquals("Введена неверная дата релиза (раньше 1895-12-28)", exception.getMessage());
        }

}
