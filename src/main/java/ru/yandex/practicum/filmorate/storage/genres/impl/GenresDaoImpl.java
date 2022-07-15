package ru.yandex.practicum.filmorate.storage.genres.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.GenresDao;

import java.util.List;

@Repository
public class GenresDaoImpl implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenresFromDb() {
        String sql = "SELECT genre_id, genre_name FROM genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre getGenreByIdFromDb(Integer id) {
        String sql = "SELECT genre_id, genre_name FROM genre WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sql, new GenreMapper(), id);
    }
}
