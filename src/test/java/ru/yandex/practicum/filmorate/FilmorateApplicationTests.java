package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }
//    private final UserDbStorage userStorage;
//    private final FilmDbStorage filmStorage;
//
//    User userBefore = new User(1,
//            "user1@email.ru",
//            "User1login",
//            "user1name",
//            LocalDate.of(1990, 12, 15));
//
//    Film filmBefore = new Film(1,
//            "Фильм1",
//            "Описание фильма 1",
//            LocalDate.of(2022, 11, 5),
//            120,
//            new Mpa(1, "G"),
//            null
//    new Genre ("Комедия"));
//
//    List<Genre> genres = new ArrayList<>() ;
//    Genre genre = new Genre(1, "Комедия");
//
//
//    @Test
//    public void testFindUserById() {
//        genres.add(genre);
//        filmBefore.setGenres(genre);
//        userStorage.add(userBefore);
//        User userAfter = userStorage.getById(1);
//
//        assertEquals(userBefore.getId(), userAfter.getId());
//        assertEquals(userBefore.getName(), userAfter.getName());
//        assertEquals(userBefore.getBirthday(), userAfter.getBirthday());
//        assertEquals(userBefore.getEmail(), userAfter.getEmail());
//    }
//
//    @Test
//    public void testFindFilmById() {
//        filmStorage.add(filmBefore);
//        Film filmAfter = filmStorage.getById(1);
//
//        assertEquals(filmBefore.getId(), filmAfter.getId());
//        assertEquals(filmBefore.getName(), filmAfter.getName());
//        assertEquals(filmBefore.getDescription(), filmAfter.getDescription());
//        assertEquals(filmBefore.getDuration(), filmAfter.getDuration());
//
//    }
}