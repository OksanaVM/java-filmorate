package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NonNull
    @NotBlank
    private String name;

    @Size(max = 200, message = "Описание должно быть меньше 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;
}