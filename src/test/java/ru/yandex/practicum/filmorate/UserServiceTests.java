package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class UserServiceTests {
    private final UserService userService;
    private final User testUser = new User(1, "testEmail", "testLogin", "testName",
            LocalDate.of(1989, 3, 12));

    @Test
    void createAndGetTest() {
        userService.create(testUser);
    }

    @Test
    void updateTest() {
        userService.create(testUser);
        testUser.setName("updateName");
        userService.update(testUser);
    }

    @Test
    void removeFriendsTest() {
        userService.create(testUser);
        testUser.setName("friend");
        userService.create(testUser);
        userService.addFriendship(1, 2);
        userService.removeFriendship(1, 2);
        assertEquals(new ArrayList<>(), userService.getFriendsListById(1));
    }

    @Test
    void getCommonFriendsTest() {
        userService.create(testUser);
        testUser.setName("friend");
        userService.create(testUser);
        testUser.setName("commonFriend");
        userService.create(testUser);
        assertEquals(new ArrayList<>(), userService.getCommonFriendsList(1, 2));
        userService.addFriendship(1, 3);
        userService.addFriendship(2, 3);
    }

    @Test
    public void testUpdateUserNotFound() {
        testUser.setId(999);
        Assertions.assertThatThrownBy(() -> userService.update(testUser)).isInstanceOf(ObjectNotFoundException.class);
    }
}