package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;


import javax.validation.constraints.*;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NonNull
    @NotBlank(message = "Не правильное название фильма")
    private final String name;
    @NotNull(message = "Отсутствует описание фильма")
    @Size(max = 200, message = "слишком длинное описание, больше 200 символов")
    private final String description;
    private final String releaseDate;
    @Positive
    @Min(value = 1, message = "Неправильная продолжительность фильма")
    private final int duration;
}
