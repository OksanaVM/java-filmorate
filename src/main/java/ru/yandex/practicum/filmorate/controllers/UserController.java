package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int idCounter = 1;
    private final List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> findAll() {
        log.info("получение списка всех пользователей");
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("создание пользователя");
        user.setId(idCounter);
        idCounter++;
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.add(user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws ValidationException {
        log.info("обновление пользователя");
        if (user.getId() == 0) {
            throw new ValidationException("User без id");
        }
        User optUser = users.stream().filter(u -> u.getId() == user.getId()).findFirst().orElseThrow();
        users.remove(optUser);
        users.add(user);
        return user;
    }
}