package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    @Autowired
    public FilmService(@Qualifier("filmDbStorage")FilmStorage filmStorage, @Qualifier("userDbStorage")UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        Film updated = filmStorage.update(film);
        if (updated == null) {
            throw new ObjectNotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        return updated;
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new ObjectNotFoundException("Фильм с id=" + id + " не найден");
        }
        return film;
    }

    public Film deleteById(int id) {
        return filmStorage.deleteById(id);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmById(filmId); // throws, if not found
        if (userStorage.getById(userId) == null) {
            throw new ObjectNotFoundException("User с id=" + userId + " не найден");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getBestFilms(int count) {
        return filmStorage.getBestFilms(count);
    }
}