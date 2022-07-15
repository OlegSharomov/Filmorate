package ru.yandex.practicum.filmorate.storage.mpa.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.List;

@Repository
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpaFromDb() {
        String sql = "SELECT mpa_id, mpa_name FROM film_mpa";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    @Override
    public Mpa getMpaByIdFromDb(Integer id) {
        String sql = "SELECT mpa_id, mpa_name FROM film_mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, new MpaMapper(), id);
    }
}
