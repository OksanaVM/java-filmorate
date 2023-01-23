package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static User testUser;
    static UserController userController;

    @BeforeAll
    static void before(){
        testUser = new User("email@mail", "login", "2000-12-12");
        testUser.setName("name");
        userController = new UserController();
    }

    @Test
    public void validationBadUser(){
        Throwable exception = assertThrows(ValidationException.class,() -> {
            userController.validate(
                    new User("email mail", "login", "2000-12-12"));
        });
        assertEquals("поле email пусто или не содержит @", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            userController.validate(
                    new User("", "login", "2000-12-12"));
        });
        assertEquals("поле email пусто или не содержит @", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            userController.validate(
                    new User("email@mail", "", "2000-12-12"));
        });
        assertEquals("поле login пусто или содержит пробелы", exception.getMessage());

        exception = assertThrows(ValidationException.class,() -> {
            userController.validate(
                    new User("email@mail", " ", "2000-12-12"));
        });
        assertEquals("поле login пусто или содержит пробелы", exception.getMessage());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        exception = assertThrows(ValidationException.class,() -> {
            userController.validate(
                    new User("email@mail", "login", LocalDate.now().plusDays(1).format(formatter)));
        });
        assertEquals("введенная дата дня рождения еще не наступила", exception.getMessage());
    }
}