package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Отсутствует email")
    @Email(message = "Некорректный email")
    private final String email;
    @NotBlank(message = "логин пуст")
    private final String login;
    private String name;
    @NotNull(message = "Не указана дата рождения")
    @PastOrPresent(message = "Некорректная дата рождения")
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
    public User(int id, @NonNull String email, @NonNull String login, String name, @NonNull LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
