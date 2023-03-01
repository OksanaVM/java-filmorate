package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa get(int id) {
        Mpa mpa = mpaStorage.findById(id);
        if (mpa != null) {
            return mpa;
        } else {
            throw new ObjectNotFoundException("Такого MPA не существует");
        }
    }

    public Collection<Mpa> getAll() {
        return mpaStorage.findALl();
    }
}
