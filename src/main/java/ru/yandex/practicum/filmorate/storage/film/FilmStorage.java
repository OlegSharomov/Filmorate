package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    // возвращает список всех фильмов
    List<Film> getAllFilmsFromStorage();

    //возвращает фильм по id
    Film getFilmByIdFromStorage(Long id);

    // создает новый фильм в хранилище и возвращает его в ответе
    Film addNewFilm(Film film);

    // изменяет фильм и возвращает его в ответе
    Film updateFilm(Film film);

    //проверяет наличие фильма и пользователя
    void checkContainsFilmById(Long filmId);
}
