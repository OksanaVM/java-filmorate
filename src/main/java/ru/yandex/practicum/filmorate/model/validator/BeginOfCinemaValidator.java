package ru.yandex.practicum.filmorate.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeginOfCinemaValidator implements ConstraintValidator<BeginOfCinema, LocalDate> {

    private static final LocalDate BEGIN_OF_CINEMA = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return !date.isBefore(BEGIN_OF_CINEMA);
    }
}