package ru.yandex.practicum.filmorate.service.genres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.GenresDao;

import java.util.List;

@Service
public class GenresService {
    private final GenresDao genresDao;

    @Autowired
    public GenresService(GenresDao genresDao) {
        this.genresDao = genresDao;
    }

    public List<Genre> getAllGenres() {
        return genresDao.getAllGenresFromDb();
    }

    public Genre getGenreById(Integer id) {
        if (id <= 0 || id > 6) {
            throw new ObjectNotFoundException("Не правильно указан id запрашиваемого жанра. Значение должно быть от 1 до 6");
        }
        return genresDao.getGenreByIdFromDb(id);
    }
}
