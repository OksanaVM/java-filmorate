package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idCounter = 1;
    private final static LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> findAll() {
        log.info("получение всех фильмов");
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("добавление фильма");
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new ValidationException("Дата не валидна");
        }
        film.setId(idCounter);
        idCounter++;
        films.add(film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("обновление фильма");
        films.remove(film);
        films.add(film);
        return film;
    }
}