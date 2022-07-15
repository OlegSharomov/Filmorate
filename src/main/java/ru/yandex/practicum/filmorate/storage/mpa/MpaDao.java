package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    List<Mpa> getAllMpaFromDb();

    Mpa getMpaByIdFromDb(Integer id);
}
