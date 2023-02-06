package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exeption.ObjectNotFoundException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    private int genId() {
        return ++id;
    }

    @Override
    public List<User> findAll() {
        log.debug("Текущее количество юзеров: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        log.info("Запрос POST /users/{}", user);
        user.setId(genId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Запрос PUT /users/{}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ObjectNotFoundException("User с id " + id + " не найден");
        }
        return user;
    }

    @Override
    public User getById(int id) {
        log.info("Запрос Get /users/{}", id);
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ObjectNotFoundException("User с id=" + id + " не найден");
        }
    }

    @Override
    public User deleteById(int id) {
        log.info("Запрос Del /users/{}", id);
        if (users.containsKey(id)) {
            User user = users.get(id);
            users.remove(id);
            return user;
        } else {
            throw new ObjectNotFoundException("User с id=" + id + " не найден");
        }
    }

    @Override
    public List<Integer> addFriendship(int firstId, int secondId) {
        log.info("Запрос PUT /users/{}/friends/{} ", firstId, secondId);
        if (!users.containsKey(firstId)) {
            throw new ObjectNotFoundException("User с id=" + firstId + " не найден");
        } else if (!users.containsKey(secondId)) {
            throw new ObjectNotFoundException("User с id=" + secondId + " не найден");
        } else {
            users.get(firstId).getFriends().add(secondId);
            users.get(secondId).getFriends().add(firstId);
            return new ArrayList<>(users.get(firstId).getFriends());
        }
    }

    @Override
    public List<Integer> removeFriendship(int firstId, int secondId) {
        log.info("Запрос DELETE /users/{}/friends/{} ", firstId, secondId);
        users.get(firstId).getFriends().remove(secondId);
        users.get(secondId).getFriends().remove(firstId);
        return new ArrayList<>(users.get(firstId).getFriends());
    }

    @Override
    public List<User> getFriendsListById(int id) {
        log.info("Запрос GET /users/{}/friends", id);
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("User с id=" + id + " не найден");
        } else {
            return users.get(id).getFriends().stream().map(users::get).collect(Collectors.toList());
        }
    }

    @Override
    public List<User> getCommonFriendsList(int firstId, int secondId) {
        log.info("Запрос GET /users/{}/friends/common/{}", firstId, secondId);
        if (!users.containsKey(firstId)) {
            throw new ObjectNotFoundException("User с id=" + firstId + " не найден");
        } else if (!users.containsKey(secondId)) {
            throw new ObjectNotFoundException("User с id=" + secondId + " не найден");
        } else {
            return users.get(firstId).getFriends().stream().filter(id -> users.get(secondId).getFriends().contains(id)).
                    map(users::get).collect(Collectors.toList());
        }
    }
}