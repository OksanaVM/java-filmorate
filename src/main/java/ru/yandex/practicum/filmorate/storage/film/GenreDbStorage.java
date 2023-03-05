package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DbStorageMixin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage, DbStorageMixin {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public Collection<Genre> findAll() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public Genre findById(int id) {
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        return queryForObjectOrNull(sqlQuery, this::makeGenre, id);
    }

    private Genre makeGenre(ResultSet rs, int rn) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }

    public Map<Integer, List<Genre>> getFilmGenres() {
        String query = "select * from film_genre join genre on genre.genre_id = film_genre.genre_id";
        List<FilmGenre> filmGenreList = jdbcTemplate.query(query, getRowMapperFilmGenres());
        Map<Integer, List<Genre>> filmGenres = new HashMap<>();
        for (FilmGenre filmGenre : filmGenreList) {
            if (!filmGenres.containsKey(filmGenre.getFilmId())) {
                filmGenres.put(filmGenre.getFilmId(), new ArrayList<>());
            }
            filmGenres.get(filmGenre.getFilmId()).add(filmGenre.getGenre());
        }
        return filmGenres;
    }


    private RowMapper<FilmGenre> getRowMapperFilmGenres() {
        return (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return new FilmGenre(rs.getInt("film_id"), genre);
        };
    }


    @Getter
    @AllArgsConstructor
    private static class FilmGenre {
        Integer filmId;
        Genre genre;
    }
}
