package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilmsFromStorage() {
        String sql = "SELECT f.film_id, f.film_name, f.film_description, f.film_release_date, f.film_duration, " +
                "f.mpa_id, m.mpa_name, f.rating " +
                "FROM films AS f " +
                "LEFT JOIN film_mpa AS m ON f.mpa_id = m.mpa_id ";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Optional<Film> getFilmByIdFromStorage(Long id) {
        String sql = "SELECT f.film_id, f.film_name, f.film_description, f.film_release_date, f.film_duration, " +
                "f.mpa_id, m.mpa_name, f.rating " +
                "FROM films AS f " +
                "LEFT JOIN film_mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE film_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            Film film = Film.builder()
                    .id(rs.getLong("film_id"))
                    .name(rs.getString("film_name"))
                    .description(rs.getString("film_description"))
                    .releaseDate(Objects.requireNonNull(rs.getDate("film_release_date")).toLocalDate())
                    .duration(rs.getInt("film_duration"))
                    .rate(rs.getInt("rating"))
                    .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                    .build();
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Film addNewFilm(Film film) {
        String sql = "INSERT INTO films (film_name, film_description, film_release_date, film_duration, mpa_id, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            ps.setInt(6, film.getRate());
            return ps;
        }, keyHolder);
        return getFilmByIdFromStorage(Objects.requireNonNull(keyHolder.getKey()).longValue()).get();
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET film_name = ?, film_description = ?, film_release_date = ?, " +
                "film_duration = ?, mpa_id = ?, rating = ? WHERE film_id = ? ;";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        return getFilmByIdFromStorage(film.getId()).get();
    }

    @Override
    public void checkContainsFilmById(Long filmId) {
        String sql = "SELECT film_name FROM films WHERE film_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, filmId);
        if (!rs.next()) {
            throw new ObjectNotFoundException(String.format("Фильм с переданным id = %d не найден.", filmId));
        }
    }
}
