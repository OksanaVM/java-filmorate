package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Data
@RequiredArgsConstructor
public class Genre {
    private  int id;
    private String name;
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
