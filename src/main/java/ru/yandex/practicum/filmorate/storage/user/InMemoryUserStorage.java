package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exeption.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        users.containsKey(id);
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }
}