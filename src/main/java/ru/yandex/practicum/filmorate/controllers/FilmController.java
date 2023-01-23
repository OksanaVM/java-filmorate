package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idCounter = 1;
    private final static LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.info("получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("добавление фильма");
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new ValidationException("Дата не валидна");
        }
        film.setId(idCounter);
        idCounter++;
        addFilm(film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws ValidationException {
        log.info("обновление фильма");
        if (film.getId() == 0) {
            throw new ValidationException("film без id");
        }
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("film не найден");
        }
        addFilm(film);
        return film;
    }

    private void addFilm(Film film) {
        films.put(film.getId(), film);
    }
}