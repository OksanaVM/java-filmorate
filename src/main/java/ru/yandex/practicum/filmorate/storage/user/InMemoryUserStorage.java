package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static ru.yandex.practicum.filmorate.Util.emptyIfNull;

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
    public Optional<User> update(User user) {
        log.info("Запрос PUT /users/{}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
        return Optional.of(user);
    }


    @Override
    public User getById(int id) {
        log.info("Запрос Get /users/{}", id);
        return users.get(id);
    }

    @Override
    public User deleteById(int id) {
        log.info("Запрос Del /users/{}", id);
        return users.remove(id);
    }

    @Override
    public boolean addFriend(int id, int friendId) {
        User u = users.get(id);
        if (u == null  ||  users.get(friendId) == null) {
            throw new ObjectNotFoundException("Object not found");
        }
        Set<Integer> friends = emptyIfNull(u.getFriends());
        friends.add(friendId);
        u.setFriends(friends);
        return true;
    }

    @Override
    public boolean ackFriend(int id, int friendId) {
        User u = users.get(id);
        if (u == null) {
            return false;
        }
        return emptyIfNull(u.getFriends()).contains(friendId);
    }

    @Override
    public boolean deleteFriend(int id, int friendId) {
        User u = users.get(id);
        if (u == null) {
            return false;
        }
        return emptyIfNull(u.getFriends()).remove(friendId);
    }

    @Override
    public List<User> findFriends(int id) {
        User u = users.get(id);
        if (u == null) {
            return emptyList();
        }
        return emptyIfNull(u.getFriends()).stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int id1, int id2) {
        User u1 = users.get(id1);
        User u2 = users.get(id2);
        if (u1 == null  ||  u2 == null) {
            return emptyList();
        }
        Set<Integer> friends2 = emptyIfNull(u2.getFriends());
        return emptyIfNull(u1.getFriends()).stream()
                .filter(friends2::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }
}
