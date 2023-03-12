package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmServiceTests {
    private final FilmService filmService;
    private final UserService userService;
    private final Film film = new Film(1, "name", "description1",
            LocalDate.of(2023, 1, 19), 100, null, new Mpa(1, "G"));
    private final User user = new User(1, "testEmail", "testLogin", "testName",
            LocalDate.of(1989, 3, 12));

    @Test
    public void addAndGetFilmTest() {
        filmService.create(film);
        assertEquals (film, filmService.getFilmById(film.getId()));
    }

    @Test
    public void findAllFilmsTest() {
        filmService.create(film);
        List <Film> allFilms = filmService.findAll();
        assertEquals (1, allFilms.size());
    }


    @Test
    public void updateFilmTest() {
        filmService.create(film);
        film.setMpa(new Mpa(3, "PG-13"));
        filmService.update(film);
        Film f = filmService.getFilmById(film.getId());
        assertEquals ("PG-13", f.getMpa().getName());
    }

    @Test
    public void deleteFilmTest() {
        filmService.create(film);
        filmService.deleteById(film.getId());
        Assertions.assertThatThrownBy(() -> filmService.getFilmById(film.getId())).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    public void testUpdateFilmNotFound() {
        film.setId(999);
        Assertions.assertThatThrownBy(() -> filmService.update(film)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    public void addAndDeleteLikeTest() {
        filmService.create(film);
        Film film2 = new Film(2, "name2", "description1",
                LocalDate.of(2023, 1, 19), 100, null, new Mpa(1, "G"));
        filmService.create(film2);
        userService.create(user);
        filmService.addLike(1, 1);
        assertEquals(List.of(film, film2), filmService.getBestFilms(3));
        filmService.removeLike(1, 1);
        filmService.addLike(2, 1);
        assertEquals(List.of(film2, film), filmService.getBestFilms(3));
    }
}

