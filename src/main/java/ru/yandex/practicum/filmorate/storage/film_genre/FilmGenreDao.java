package ru.yandex.practicum.filmorate.storage.film_genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.TreeSet;

public interface FilmGenreDao {
    TreeSet<Genre> getAllGenresByFilmId(Long id);

    void addGenre(Long filmId, Integer genreId);

    void removeAllGenresByIdFilm(Long filmId);
}
