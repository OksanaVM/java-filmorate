package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage{
    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getById(int id) {
        return null;
    }

    @Override
    public Film deleteById(int id) {
        return null;
    }

    @Override
    public List<Film> getBestFilms(int count) {
        return null;
    }
}
