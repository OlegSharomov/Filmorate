package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long countId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    private Long setIdOfFilm() {
        return ++countId;
    }

    @Override
    public List<Film> getAllFilmsFromStorage() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmByIdFromStorage(Long id) {
        return films.get(id);
    }

    @Override
    public Film addNewFilm(Film film) {
        film.setId(setIdOfFilm());
        films.put(film.getId(), film);
        log.debug(String.format("Фильм %s успешно добавлен", film));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.debug(String.format("Фильм %s успешно обновлен", film));
        return film;
    }

    @Override
    public void checkContainsFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new ObjectNotFoundException(String.format("Фильм с переданным id = %d не найден.", filmId));
        }
    }
}
