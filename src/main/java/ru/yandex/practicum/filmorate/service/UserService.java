package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.*;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public void update(User user) {
        validate(user);
        if (getById(user.getId()) == null) {
            throw new ObjectNotFoundException("Для обновления пользователя необходимо передать его корректный id");
        }
        userStorage.update(user);
    }

    public User getById(int id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException("User с id=" + id + " не найден");
        }
        return user;
    }

    public User deleteById(int id) {
        return userStorage.deleteById(id);
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("В логине пробел");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public Boolean addFriendship(int id, int friendId) {
        return userStorage.addFriend(id, friendId);
//        if (getById(id) == null) {
//            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", id));
//        }
//        User user = userStorage.getById(id);
//        if (getById(friendId) == null) {
//            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", friendId));
//        }
//        User newFriend = userStorage.getById(friendId);
//        if (id.equals(friendId)) {
//            throw new ValidationException("Пользователь не может добавть в друзья сам себя");
//        }
//        user.getFriends().add(friendId);
//        newFriend.getFriends().add(id);
//        return true;
    }

    public Boolean removeFriendship(Integer id, Integer friendId) throws ObjectNotFoundException {
        return userStorage.deleteFriend(id, friendId);
//        if (getById(id) == null) {
//            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", id));
//        }
//        User user = userStorage.getById(id);
//        if (getById(friendId) == null) {
//            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", friendId));
//        }
//        User removedFriend = userStorage.getById(friendId);
//        if (!user.getFriends().contains(friendId) || !removedFriend.getFriends().contains(id)) {
//            throw new ObjectNotFoundException("Пользователи не являются друзьями");
//        }
//        user.getFriends().remove(friendId);
//        removedFriend.getFriends().remove(id);
//        return true;
    }

    public List<User> getFriendsListById(int id) {
        return userStorage.findFriends(id);
//        User user = userStorage.getById(id);
//        return user.getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    private User getUserOrThrow(int id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не добавлен в систему", id));
        }
        return user;
    }

    public List<User> getCommonFriendsList(int userId, int friendId) {
        return userStorage.findCommonFriends(userId, friendId);
//        User user = getUserOrThrow(userId);
//        User otherUser = getUserOrThrow(friendId);
//        return user.getFriends().stream()
//                .filter(u -> otherUser.getFriends().contains(u))
//                .map(userStorage::getById)
//                .collect(Collectors.toList());
    }
}