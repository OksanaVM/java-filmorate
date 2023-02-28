package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService ratingService;

    public MpaController(MpaService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public Collection<Mpa> getAll() {
        return ratingService.getAll();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable int id) {
        return ratingService.get(id);
    }
}
