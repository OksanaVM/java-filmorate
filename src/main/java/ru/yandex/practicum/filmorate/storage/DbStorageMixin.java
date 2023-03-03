package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;

public interface DbStorageMixin {
    JdbcTemplate getJdbcTemplate();

    default <T> T queryForObjectOrNull(String sql, RowMapper<T> rowMapper, Object... args)
            throws DataAccessException
    {
        try {
            return getJdbcTemplate().queryForObject(sql, rowMapper, args);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    default int update(String sql, Object... args) throws DataAccessException {
        try {
            return getJdbcTemplate().update(sql, args);
        } catch (DataIntegrityViolationException ex) {
            throw new ObjectNotFoundException("Object not found");
        }
    }

    default void updateIgnoreDuplicate(String sql, Object... args) throws DataAccessException {
        try {
            getJdbcTemplate().update(sql, args);
        } catch (DuplicateKeyException ignore) {}
    }
}
