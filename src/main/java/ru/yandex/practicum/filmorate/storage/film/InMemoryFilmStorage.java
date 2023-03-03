package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Util.emptyIfNull;

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
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(int id) {
        log.info("Запрос Get /films/{}", id);
        return films.get(id);
    }

    @Override
    public Film deleteById(int id) {
        log.info("Запрос Del /films/{} ", id);
        return films.remove(id);
    }

    @Override
    public List<Film> getBestFilms(int count) {
        log.info("Запрос Get /films/popular?count={}", count);
        return films.values().stream().sorted((a, b) -> b.getLikes().size() - a.getLikes().size()).
                limit(count).collect(Collectors.toList());
    }

    @Override
    public void addLike(int id, int userId) {
        Film f = getById(id);
        Set<Integer> likes = emptyIfNull(f.getLikes());
        likes.add(userId);
        f.setLikes(likes);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        emptyIfNull(getById(id).getLikes()).remove(userId);
    }
}
