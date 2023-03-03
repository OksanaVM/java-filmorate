package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(int id) {
        Genre g = genreStorage.findById(id);
        if (g == null) {
            throw new ObjectNotFoundException("Жанр с идентификатором " + id + " не найден.");
        }
        return g;
    }

    public Collection<Genre> getAll() {
        return genreStorage.findAll();
    }
}