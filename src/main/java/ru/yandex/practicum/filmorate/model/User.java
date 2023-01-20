package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Past;

@Data
public class User {

    private int id;

    @NonNull
    @NotBlank
    @Email(message = "Почта должна быть валидна")
    private final String email;

    @NonNull
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин должен быть без пробелов")
    private String login;

    private String name;

    @Past(message = "День рождения должен быть в прошлом")
    private LocalDate birthday;
}