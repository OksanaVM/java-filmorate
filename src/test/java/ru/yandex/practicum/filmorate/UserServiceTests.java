package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTests {
    private final UserService userService;
    private final User testUser = new User(1, "testEmail", "testLogin", "testName",
            LocalDate.of(1989, 3, 12));
    private final User testUser2 = new User(2, "testEmail2", "testLogin2", "testName2",
            LocalDate.of(1990, 4, 12));
    private final User testUser3 = new User(3, "testEmail3", "testLogin3", "testName3",
            LocalDate.of(1991, 5, 12));

    @Test
    void createAndGetTest() {
        userService.create(testUser);
        assertEquals(testUser, userService.getById(testUser.getId()));
    }

    @Test
    void updateTest() {
        userService.create(testUser);
        testUser.setName("updateName");
        userService.update(testUser);
        assertEquals(testUser.getName(), userService.getById(testUser.getId()).getName());
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
        userService.create(testUser2);
        userService.create(testUser3);
        userService.addFriendship(1, 3);
        userService.addFriendship(2, 3);
        List <User> listCommonFriends = userService.getCommonFriendsList(1, 2);
        assertEquals(1,  listCommonFriends.size());
        assertTrue(listCommonFriends.contains(testUser3));
      }

    @Test
    public void testUpdateUserNotFound() {
        testUser.setId(999);
        Assertions.assertThatThrownBy(() -> userService.update(testUser)).isInstanceOf(ObjectNotFoundException.class);
    }
}