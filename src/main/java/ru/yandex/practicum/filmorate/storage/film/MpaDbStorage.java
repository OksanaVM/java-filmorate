package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        final String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa";
        log.info("Запрос всех mpa выполнен");
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Mpa getById(int id) {
        final String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!mpaRows.next()) {
            log.info("Mpa с id = {} не найден.", id);
            throw new ObjectNotFoundException("Mpa не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, id);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        return new Mpa(mpaId, mpaName);
    }
}

