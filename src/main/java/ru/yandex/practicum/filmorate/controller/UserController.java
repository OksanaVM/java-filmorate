package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    private int generatorId() {
        return ++id;
    }


    @GetMapping
    public List<User> findAll() {
        log.debug("Получен запрос на список пользоваталей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseBody
    public User create(@Valid @RequestBody User user) throws Exception {
        log.info("Запрос POST /users " + user);
        if (validate(user)) {
            user.setId(generatorId());
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    @ResponseBody
    public User update(@Valid @RequestBody User user) throws Exception {
        log.info("Запрос PUT /users " + user);
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new Exception();
        }
        return user;
    }

    boolean validate(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта пользователя пустая или не содержит @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин пустой или содержит пробелы");
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(user.getBirthday(), inputFormatter);
        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения пользователя превышает текущую дату");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return true;
    }
}