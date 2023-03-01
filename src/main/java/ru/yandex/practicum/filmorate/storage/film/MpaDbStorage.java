package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findById(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE mpa_id= ?", id);

        if (rowSet.next()) {
            return new Mpa(
                     rowSet.getInt("mpa_id"),
                    rowSet.getString("mpa_name"));
        } else {
            String message = "Рейтинг с идентификатором " + id + " не найден.";
            log.info(message);
            throw new ObjectNotFoundException(message);
        }
    }

    @Override
    public Collection<Mpa> findALl() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, (rs, rn) -> makeRating(rs));
    }

    private Mpa makeRating(ResultSet rs) throws SQLException {
        return new Mpa((Integer) rs.getInt("mpa_id"), rs.getString("mpa_name"));
    }
}

