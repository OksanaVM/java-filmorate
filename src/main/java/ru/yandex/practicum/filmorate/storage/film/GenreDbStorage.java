package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.DbStorageMixin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

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
}
