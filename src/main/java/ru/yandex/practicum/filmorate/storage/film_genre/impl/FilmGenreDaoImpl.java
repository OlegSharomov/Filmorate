package ru.yandex.practicum.filmorate.storage.film_genre.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDao;

import java.util.TreeSet;
import java.util.stream.Collectors;

@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TreeSet<Genre> getAllGenresByFilmId(Long id) {
        String sql = "SELECT g.genre_id, g.genre_name " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?;";
        return jdbcTemplate
                .queryForStream(sql, new GenreMapper(), id).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void addGenre(Long filmId, Integer genreId) {
        String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES ( ?, ? );";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    @Override
    public void removeAllGenresByIdFilm(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sql, filmId);
    }
}