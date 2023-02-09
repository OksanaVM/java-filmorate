package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exeption.ObjectNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    private int genId() {
        return ++id;
    }

    @Override
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        log.info("Запрос POST /films/{}", film);
        film.setId(genId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        log.info("Запрос PUT /films/{}", film);
        films.containsKey(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(int id) {
        log.info("Запрос Get /films/{}", id);
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ObjectNotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    @Override
    public Film deleteById(int id) {
        log.info("Запрос Del /films/{} ", id);
        films.containsKey(id);
        Film film = films.get(id);
        films.remove(id);
        return film;

    }


    @Override
    public List<Film> getBestFilms(int count) {
        log.info("Запрос Get /films/popular?count={}", count);
        return films.values().stream().sorted((a, b) -> b.getLikes().size() - a.getLikes().size()).
                limit(count).collect(Collectors.toList());
    }


}