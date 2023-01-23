package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Film {
    private int id;
    @NonNull
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    private final String releaseDate;
    private final int duration;
}
