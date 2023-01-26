package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Data
public class User {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Отсутствует email")
    @Email(message = "Некорректный email")
    private final String email;
    @NotBlank(message = "логин пуст")
    @NotNull(message = "Отсутствует логин")
    private final String login;
    private String name;
    private final String birthday;
}
