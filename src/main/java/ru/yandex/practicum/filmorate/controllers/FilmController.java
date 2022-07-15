package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> showAllFilms() {
        log.debug("Получен запрос к эндпоинту GET /films");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        log.debug("Получен запрос к эндпоинту POST /films с переданным телом: '{}'", film);
        try {
            if (films.containsKey(film.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Данный фильм уже существует");
            }
            if (isCorrectFilm(film)) {
                films.put(film.getId(), film);
                log.debug("Фильм успешно добавлен");
            }
        } catch (NullPointerException | ValidationException ex) {
            log.warn("При обработке запроса к эндпоинту POST /films произошла ошибка: '{}'", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Получен запрос к эндпоинту PUT к эндпоинту /films с переданным телом: '{}'", film);
        try {
            if (isCorrectFilm(film)) {
                films.put(film.getId(), film);
                log.debug("Фильм успешно обновлен");
            }
        } catch (NullPointerException | ValidationException ex) {
            log.warn("При обработке запроса к эндпоинту PUT /films произошла ошибка: '{}'", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return film;
    }

    public boolean isCorrectFilm(Film film) throws ValidationException {
        LocalDate birthdayMovie = LocalDate.of(1895, 12, 28);
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не корректно");
        } else if (film.getDescription().length() > 200 || film.getDescription().isBlank()) {
            throw new ValidationException("Описание фильма не должно быть пустым или больше 200 символов");
        } else if (film.getReleaseDate().isBefore(birthdayMovie)) {
            throw new ValidationException("Дата релиза указана не корректно");
        } else if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
