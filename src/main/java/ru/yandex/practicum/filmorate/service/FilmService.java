package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

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
        return filmStorage.getById(id);
    }

    public Film deleteById(int id) {
        return filmStorage.deleteById(id);
    }

    public void addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (film != null && userId > 0) {
            film.getLikes().add(userId);
            filmStorage.update(film);
        } else {
            throw new ObjectNotFoundException("Пользователя с таким id не существует");
        }
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