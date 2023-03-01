package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.validator.BeginOfCinema;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Не правильное название фильма")
    private final String name;
    @NotNull(message = "Отсутствует описание фильма")
    @Size(max = 200, message = "слишком длинное описание, больше 200 символов")
    private final String description;
    @NotNull(message = "Отсутствует дата релиза фильма фильма")
    @BeginOfCinema
    private final LocalDate releaseDate;
    @Min(value = 1, message = "Неправильная продолжительность фильма")
    private final int duration;
    private Set<Integer> likes = new HashSet<>();
    private List<Genre> genres;
    private Mpa mpa;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, List<Genre> genres, Mpa mpa) {
        this.id = id;
        this.description= description;
        this.releaseDate= releaseDate;
        this.name = name;
        this.duration = duration;
        this.genres = genres;
        this.mpa= mpa;
    }
}
