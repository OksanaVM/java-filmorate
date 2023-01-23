package ru.yandex.practicum.filmorate.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
public class User {
    private int id;
    @Email
    private final String email;
    @NotBlank
    @NonNull
    private final String login;
    private String name;
    private final String birthday;
}
