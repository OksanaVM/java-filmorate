package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
@AllArgsConstructor
public class Mpa {
    private final int id;
    private String name;

    public void setId(int mpa_id) {
    }
}
