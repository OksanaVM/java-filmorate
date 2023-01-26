package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UsersTestController {
    private UserController userController;

    @BeforeEach
    void before() {
        userController = new UserController();
    }

    @Test
        public void validationBadDateUser(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Throwable exception = assertThrows(ValidationException.class, () -> userController.validate(
                new User("email@mail", "login", LocalDate.now().plusDays(1).format(formatter))));
        assertEquals("Дата рождения пользователя превышает текущую дату", exception.getMessage());
    }

    @Test
    public void validationBlankNameUser() throws ValidationException {
        User testUser = new User("email@mail", "login", "2000-12-12");
        userController.validate(testUser);
        assertEquals(testUser.getName(), testUser.getLogin());

}
}