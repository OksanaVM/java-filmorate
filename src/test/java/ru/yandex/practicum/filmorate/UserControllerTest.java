package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerTest {

    static UserController userController;
    private Validator validator;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void putWithoutId() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Юзер 1")
                .login("User")
                .build();
        ValidationException thrown = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.put(user));
        assertTrue(thrown.getMessage().contains("User без id"));
    }

    @Test
    void putUnexisted() {
        User user = User.builder()
                .id(1)
                .email("mail@mail.ru")
                .name("Юзер 1")
                .login("User")
                .build();
        ValidationException thrown = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.put(user));
        assertTrue(thrown.getMessage().contains("User не найден"));
    }

    @Test
    void createBlankEmail() {
        User user = User.builder()
                .email("")
                .name("Юзер 1")
                .login("User")
                .birthday(LocalDate.parse("1987-06-07"))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(i-> System.out.println(i.getMessage()));
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("не должно быть пустым")));
    }

    @Test
    void createInvalidEmail() {
        User user = User.builder()
                .email("@mail.ru")
                .name("Юзер 1")
                .login("Userr")
                .birthday(LocalDate.parse("1987-06-07"))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("Почта должна быть валидна")));
    }

    @Test
    void createBlankLogin() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Юзер 1")
                .login("")
                .birthday(LocalDate.parse("1987-06-07"))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("не должно быть пустым")));
    }

    @Test
    void createInvalidLogin() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Юзер 1")
                .login("User luser")
                .birthday(LocalDate.parse("1987-06-07"))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("Логин должен быть без пробелов")));
    }

    @Test
    void createInvalidBirthday() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Юзер 1")
                .login("Usess")
                .birthday(LocalDate.parse("2087-06-07"))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(i -> i.getMessage().equals("День рождения должен быть в прошлом")));
    }
}