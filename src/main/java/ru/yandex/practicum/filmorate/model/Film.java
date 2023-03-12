package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.validator.BeginOfCinema;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Не правильное название фильма")
    private String name;
    @NotNull(message = "Отсутствует описание фильма")
    @Size(max = 200, message = "слишком длинное описание, больше 200 символов")
    private String description;
    @NotNull(message = "Отсутствует дата релиза фильма фильма")
    @BeginOfCinema
    private LocalDate releaseDate;
    @Min(value = 1, message = "Неправильная продолжительность фильма")
    private int duration;
    private List<Genre> genres;
    private Set<Integer> likes;
    private Mpa mpa;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, List<Genre> genres, Mpa mpa) {
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.name = name;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.name = name;
        this.duration = duration;

        this.mpa = mpa;
    }
}
