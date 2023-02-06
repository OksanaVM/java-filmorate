package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;


    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
        public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
      public Film updateFilm(@Valid @RequestBody Film film)  {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Positive @RequestParam(defaultValue = "10") int count) {
        return filmService.getBestFilms(count);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id)  {
        return filmService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Film deleteById(@PathVariable int id) {
        return filmService.deleteById(id);
    }
}
