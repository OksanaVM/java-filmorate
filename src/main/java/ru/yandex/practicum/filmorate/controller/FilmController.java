package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate MINDATA = LocalDate.of(1895, 12, 28);

    private int genId() {
        return ++id;
    }


    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    @ResponseBody
    public Film addFilm(@Valid @RequestBody Film film) throws Exception {
        log.info("Запрос POST /films " + film);
        if (validate(film) && !films.containsKey(film.getId())) {
            film.setId(genId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    @ResponseBody
    public Film updateFilm(@Valid @RequestBody Film film) throws Exception {
        log.info("Запрос PUT /films " + film);
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new Exception();
        }
        return film;
    }

    boolean validate(Film film) throws Exception {
        if (film.getName().isEmpty()) {
            log.debug("Не введено название фильма!");
            throw new ValidationException("Не введено название фильма!");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Превышено максимальное кол-во символов в описании!");
            throw new ValidationException("Превышено максимальное кол-во символов в описании!");
        }
        if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма меньше либо равна нулю");
            throw new ValidationException("Продолжительность фильма меньше либо равна нулю");
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releaseDate = LocalDate.parse(film.getReleaseDate(), inputFormatter);
        if (releaseDate.isBefore(MINDATA)) {
            log.debug("Введена неверная дата релиза (раньше 1895-12-28)");
            throw new ValidationException("Введена неверная дата релиза (раньше 1895-12-28)");
        }
        return true;
    }
}