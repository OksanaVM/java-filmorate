package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {


    private final JdbcTemplate jdbcTemplate;
    private Integer idMax = 0;

    public Integer getIdMax() {
        idMax = idMax + 1;
        return idMax;
    }

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        final String sqlQuery = "SELECT * FROM users";
        log.info("запрос чтения всех user отправлен");
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User create(User user) {
        final String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES ( ?, ?, ?, ?)";
        KeyHolder generatedId = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, generatedId);
        user.setId(Objects.requireNonNull(generatedId.getKey().intValue()));
        log.info("запрос создания user с id {} отправлен", user.getId());
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        final String sqlQuery = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        log.info("запрос на обновление user с id = {} отправлен.", user.getId());
        return Optional.of(user);
    }

    @Override
    public User getById(int id) {
        final String sqlQuery = "SELECT * FROM users WHERE id = ?";
        log.info("запрос на получение user с id = {} отправлен.", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, id);
    }

    @Override
    public User deleteById(int id) {
        final String sqlQuery = "SELECT * FROM users WHERE id = ?";
        final String sqlDeleteQuery = "DELETE FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sqlQuery, this::makeUser, id);
        jdbcTemplate.update(sqlDeleteQuery, id);
        log.info("запрос на удаление user с id = {} отправлен", id);
        return user;
    }


    public List<Integer> addFriendship(int firstId, int secondId) {
        final String sqlUpdateQuery = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?";
        final String sqlWriteQuery = "INSERT INTO friends (user_id, friend_id, status ) VALUES (?, ?, ?)";
        final String checkQuery = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, firstId, secondId);
        if (userRows.next()) {
            jdbcTemplate.update(sqlUpdateQuery, true, firstId, secondId);
            log.info("user с id = {} подтвердил дружбу с user id = {}", firstId, secondId);
        } else {
            jdbcTemplate.update(sqlWriteQuery, firstId, secondId, false);
            log.info("user с id = {} подписался на user id = {}", firstId, secondId);
        }
        return List.of(firstId, secondId);
    }



    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }


}