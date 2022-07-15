package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.List;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpaFromDb();
    }

    public Mpa getMpaById(Integer id) {
        if (id <= 0 || id > 5) {
            throw new ObjectNotFoundException("Не правильно указан id запрашиваемого MPA. Значение должно быть от 1 до 5");
        }
        return mpaDao.getMpaByIdFromDb(id);
    }
}
