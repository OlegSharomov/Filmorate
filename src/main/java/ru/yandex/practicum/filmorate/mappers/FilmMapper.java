package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(Objects.requireNonNull(rs.getDate("film_release_date")).toLocalDate())
                .duration(rs.getInt("film_duration"))
                .rate(rs.getInt("rating"))
                .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .build();
    }
}
