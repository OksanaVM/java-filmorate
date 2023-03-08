package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GenreStorage {
    Collection<Genre> findAll();
    Genre findById(int id);
    Map<Integer, List<Genre>> getFilmGenres();
    Map<Integer, List<Genre>> getFilmGenresForPopular(Integer count);
}