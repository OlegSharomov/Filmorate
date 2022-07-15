package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesDao {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getMostPopularFilms(Long count);
}
