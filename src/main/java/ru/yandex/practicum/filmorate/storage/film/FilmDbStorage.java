package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        final String sqlQuery = "SELECT * FROM films";
        log.info("запрос чтения всех фильмов отправлен");
        return jdbcTemplate.query(sqlQuery, this::makefilm);
    }

    @Override
    public Film create(Film film) {
        final String sqlQuery = "INSERT INTO films (name, description, release_date, duration) VALUES ( ?, ?, ?, ?)";
        final String sqlGenreQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES ( ?, ?)";
        final String sqlMpaQuery = "INSERT INTO mpa_films (film_id, mpa_id) VALUES ( ?, ?)";
        KeyHolder generatedId = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, generatedId);
        film.setId(Objects.requireNonNull(generatedId.getKey().intValue()));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String checkDuplicate = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";
                SqlRowSet checkRows = jdbcTemplate.queryForRowSet(checkDuplicate, film.getId(), genre.getId());
                if (!checkRows.next()) {
                    jdbcTemplate.update(sqlGenreQuery, film.getId(), genre.getId());
                }
            }
        }
       jdbcTemplate.update(sqlMpaQuery, film.getId(), film.getMpa().getId());
        film.setMpa(findMpa(film.getId()));
        film.setGenres(findGenre(film.getId()));
        log.info("добавлен фильм {}", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        final String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?" +
                "WHERE id = ?";
        final String deleteMpa = "DELETE FROM mpa_id WHERE film_id = ?";
        final String updateMpa = "INSERT INTO mpa_films (film_id, mpa_id) VALUES (?, ?)";
        jdbcTemplate.update(deleteMpa, film.getId());
        jdbcTemplate.update(updateMpa, film.getId(), film.getMpa().getId());
        if (film.getGenres() != null) {
            final String sqlDeleteGenreQuery = "DELETE FROM film_genre WHERE film_id = ?";
            final String sqlUpdateGenreQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES ( ?, ?)";
            jdbcTemplate.update(sqlDeleteGenreQuery, film.getId());
            for (Genre genre : film.getGenres()) {
                String checkDuplicate = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";
                SqlRowSet checkRows = jdbcTemplate.queryForRowSet(checkDuplicate, film.getId(), genre.getId());
                if (!checkRows.next()) {
                    jdbcTemplate.update(sqlUpdateGenreQuery, film.getId(), genre.getId());
                }
            }
        }
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getId());
        film.setMpa(findMpa(film.getId()));
        film.setGenres(findGenre(film.getId()));
        log.info("Фильм {} с id = {} обновлен", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film getById(int id) {
        final String sqlQuery = "SELECT * FROM films WHERE id = ?";
        log.info("Запрос фильма с id = {} отправлен", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::makefilm, id);
    }

    @Override
    public Film deleteById(int id) {
        Film film = getById(id);
        final String sqlDeleteQuery = "DELETE FROM films WHERE id = ?";
        final String sqlDeleteGenreQuery = "DELETE FROM film_genre WHERE film_id = ?";
        final String sqlDeleteMpaQuery = "DELETE FROM mpa_films WHERE film_id = ?";
        final String sqlDeleteLikesQuery = "DELETE FROM films_likes WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteQuery, id);
        jdbcTemplate.update(sqlDeleteGenreQuery, id);
        jdbcTemplate.update(sqlDeleteMpaQuery, id);
        jdbcTemplate.update(sqlDeleteLikesQuery, id);
        log.info("Фильм {} с id = {} удален", film.getName(), film.getId());
        return film;
    }

//    @Override
//    public Film addLike(int filmId, int userId) {
//        final String sqlQuery = "INSERT INTO films_likes (film_id, user_id) VALUES ( ?, ?)";
//        jdbcTemplate.update(sqlQuery, filmId, userId);
//        log.info("Добавлен лайк к фильмы {} от юзера {}", filmId, userId);
//        return getById(filmId);
//    }
//
//    @Override
//    public Film removeLike(int filmId, int userId) {
//        final String sqlQuery = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
//        jdbcTemplate.update(sqlQuery, filmId, userId);
//        log.info("Удален лайк к фильмы {} от юзера {}", filmId, userId);
//        return null;
//    }

    @Override
    public List<Film> getBestFilms(int count) {
        final String sqlQuery = "SELECT id, name, description, release_date, duration FROM films " +
                "LEFT JOIN films_likes fl ON films.id = fl.film_id " +
                "group by films.id, fl.film_id IN ( " +
                "    SELECT film_id " +
                "    FROM films_likes " +
                ") " +
                "ORDER BY COUNT(fl.film_id) DESC " +
                "LIMIT ?";
        log.info("Запрос {} популярных фильмов выполнен", count);
        return jdbcTemplate.query(sqlQuery, this::makefilm, count);
    }

    private Mpa findMpa(int id) {
        final String sqlQuery = "SELECT mpa.mpa_id, mpa_name FROM mpa " +
                "LEFT JOIN mpa_films ON mpa.mpa_id = mpa_films.mpa_id " +
                "WHERE film_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        mpaRows.next();
        return new Mpa(mpaRows.getInt("mpa_id"), mpaRows.getString("mpa_name"));
    }

    private List<Genre> findGenre(int id) {
        final String sqlQuery = "SELECT genre.genre_id, GENRE_NAME " +
                "FROM genre " +
                "LEFT JOIN film_genre FG on genre.genre_id = FG.GENRE_ID " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, id);

    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");
        return new Genre(id, name);
    }

    private Film makefilm(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration");
        List<Genre> genres = findGenre(id);
        Mpa mpa = findMpa(id);
        return new Film(id, name, description, releaseDate, duration, genres, mpa);
    }

}
