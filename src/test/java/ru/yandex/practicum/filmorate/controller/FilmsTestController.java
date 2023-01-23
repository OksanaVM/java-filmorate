package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
public class FilmsTestController {
    static Film testFilm;
    static FilmController filmController;

    @BeforeAll
    static void before(){
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
        Throwable exception = assertThrows(ValidationException.class,() -> {
            filmController.validate(
                    new Film("", "описание", "2003-12-12", 100));
        });
        assertEquals("Не введено название фильма!", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            filmController.validate(
                    new Film("name", "описание больше 200 символов, точнее 201!!!" +
                            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
                            "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
                            "!!!!!!!!!!!!!!!!!!!!!", "2003-12-12", 100));
        });
        assertEquals("Превышено максимальное кол-во символов в описании!", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            filmController.validate(
                    new Film("name", "описание", "1895-12-27", 100));
        });
        assertEquals("Введена неверная дата релиза (раньше 1895-12-28)", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            filmController.validate(
                    new Film("name", "описание", "1895-12-29", 0));
        });
        assertEquals("Продолжительность фильма меньше либо равна нулю", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            filmController.validate(
                    new Film("name", "описание", "1895-12-29", -1));
        });
        assertEquals("Продолжительность фильма меньше либо равна нулю", exception.getMessage());
    }

}
