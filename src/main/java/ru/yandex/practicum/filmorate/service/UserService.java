package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public void update(User user) {
        if (!userStorage.getAllUsers().containsKey(user.getId())) {
            throw new ObjectNotFoundException("Для обновления пользователя необходимо передать его корректный id");
        }
        userStorage.update(user);

    }

    public User getById(int id) {
        return userStorage.getById(id);
    }

    public User deleteById(int id) {
        return userStorage.deleteById(id);
    }

    private boolean validate(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        return false;
    }


    public Boolean addFriendship(Integer id, Integer friendId) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", id));
        }
        User user = userStorage.getById(id);
        if (!userStorage.getAllUsers().containsKey(friendId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", friendId));
        }
        User newFriend = userStorage.getById(friendId);
        if (id.equals(friendId)) {
            throw new ObjectNotFoundException("Пользователь не может добавть в друзья сам себя");
        }
        user.getFriends().add(friendId);
        newFriend.getFriends().add(id);
        return true;
    }


    public Boolean removeFriendship(Integer id, Integer friendId) throws ObjectNotFoundException {
        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", id));
        }
        User user = userStorage.getById(id);
        if (!userStorage.getAllUsers().containsKey(friendId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", friendId));
        }
        User removedFriend = userStorage.getById(friendId);
        if (!user.getFriends().contains(friendId) || !removedFriend.getFriends().contains(id)) {
            throw new ObjectNotFoundException("Пользователи не являются друзьями");
        }
        user.getFriends().remove(friendId);
        removedFriend.getFriends().remove(id);
        return true;
    }

    public List<User> getFriendsListById(int id) {
        //log.info("Запрос GET /users/{}/friends", id);
        return userStorage.getAllUsers().get(id).getFriends().stream()
                .map(u -> userStorage.getAllUsers().get(u))
                .collect(Collectors.toList());
    }


    public List<User> getCommonFriendsList(int firstId, int secondId) {
        //log.info("Запрос GET /users/{}/friends/common/{}", firstId, secondId);
        if (!userStorage.getAllUsers().containsKey(firstId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", firstId));
        }
        User user = userStorage.getById(firstId);
        if (!userStorage.getAllUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", secondId));
        }
        User otherUser = userStorage.getById(secondId);
        return user.getFriends().stream()
                .filter(u -> otherUser.getFriends().contains(u))
                .map(u -> userStorage.getAllUsers().get(u))
                .collect(Collectors.toList());
    }
}