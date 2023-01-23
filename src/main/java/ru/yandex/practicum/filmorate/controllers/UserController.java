package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int idCounter = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.info("получение списка всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("создание пользователя");
        user.setId(idCounter);
        idCounter++;
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        addUser(user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws ValidationException {
        log.info("обновление пользователя");
        if (user.getId() == 0) {
            throw new ValidationException("User без id");
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("User не найден");
        }
        addUser(user);
        return user;
    }

    private void addUser(User user) {
        users.put(user.getId(), user);
    }


}