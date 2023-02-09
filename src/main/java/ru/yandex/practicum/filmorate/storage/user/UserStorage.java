package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> findAll();

    User create(User user);

    User update(User user);

    User getById(int id);

    User deleteById(int id);

    Map<Integer, User> getAllUsers();
}