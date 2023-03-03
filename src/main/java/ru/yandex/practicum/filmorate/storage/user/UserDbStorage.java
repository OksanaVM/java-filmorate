package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorageMixin;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserDbStorage implements UserStorage, DbStorageMixin {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
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
        user.setId(Objects.requireNonNull(generatedId.getKey()).intValue());
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
        return queryForObjectOrNull(sqlQuery, this::makeUser, id);
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

    @Override
    public boolean addFriend(int id, int friendId) {
        final String sqlQuery = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        return update(sqlQuery, id, friendId, false) > 0;
    }

    @Override
    public boolean ackFriend(int id, int friendId) {
        final String sqlQuery = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?";
        return update(sqlQuery, true, id, friendId) > 0;
    }

    @Override
    public boolean deleteFriend(int id, int friendId) {
        final String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        return update(sqlQuery, id, friendId) > 0;
    }

    @Override
    public List<User> findFriends(int id) {
        final String q = "SELECT u.* FROM friends f INNER JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?";
        return jdbcTemplate.query(q, this::makeUser, id);
    }

    @Override
    public List<User> findCommonFriends(int id1, int id2) {
        final String q = "SELECT u.* FROM (" +
                "SELECT friend_id FROM friends WHERE user_id = ? INTERSECT " +
                "SELECT friend_id FROM friends WHERE user_id = ?" +
                ") f INNER JOIN users u ON f.friend_id = u.id";
        return jdbcTemplate.query(q, this::makeUser, id1, id2);
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