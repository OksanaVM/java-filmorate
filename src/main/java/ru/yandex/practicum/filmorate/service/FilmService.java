package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getById(film.getId()) != null) {
            return filmStorage.update(film);
        } else {
            throw new ObjectNotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
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

    public Film addLike(int filmId, int userId) {
        if (userStorage.getById(userId) == null) {
            throw new ObjectNotFoundException("Пользователь с ID: " + userId + " не найден");
        }
        User user = userStorage.getById(userId);
        if (filmStorage.getById(filmId)==null){
            throw new ObjectNotFoundException("Пользователь с ID: " + filmId + " не найден");
        }
        Film film = filmStorage.getById(filmId);

        film.getLikes().add(user.getId());
        return film;
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (film != null) {
            if (film.getLikes().contains(userId)) {
                film.getLikes().remove(userId);
                filmStorage.update(film);
            } else {
                throw new ObjectNotFoundException("Like на фильм " + filmId + " от пользователя "
                        + userId + " не найден");
            }
        }
    }

    public List<Film> getBestFilms(int count) {
        return filmStorage.getBestFilms(count);
    }
}