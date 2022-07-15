package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    List<Genre> getAllGenresFromDb();

    Genre getGenreByIdFromDb(Integer id);
}
