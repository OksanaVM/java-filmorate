package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class GenreServiceTests {
    private final GenreService genreService;

    @Test
    public void testGetAllGenres() {
        assertEquals(Arrays.asList(new Genre(1, "Комедия"),
                        new Genre(2, "Драма"),
                        new Genre(3, "Мультфильм"),
                        new Genre(4, "Триллер"),
                        new Genre(5, "Документальный"),
                        new Genre(6, "Боевик")),
                genreService.getAll());
    }

    @Test
    public void testGetGenreById() {
        List<Genre> list = Arrays.asList(new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик"));
        for (Genre genre : list) {
            assertEquals(genre, genreService.getGenreById(genre.getId()));
        }
    }

    @Test
    public void testGenreNotFound() {
        Genre genre = new Genre(777,"Порно");
        Assertions.assertThatThrownBy(() ->
                genreService.getGenreById(genre.getId())).isInstanceOf(ObjectNotFoundException.class);
    }
}
