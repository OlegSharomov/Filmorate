package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = inMemoryUserStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilmsFromStorage();
    }

    public Film getFilmById(Long id) {
        filmStorage.checkContainsFilmById(id);
        return filmStorage.getFilmByIdFromStorage(id);
    }

    public Film checkAndCreateFilm(Film film) {
        isCorrectFilm(film);
        return filmStorage.addNewFilm(film);
    }

    public Film checkAndUpdateFilm(Film film) {
        isCorrectFilm(film);
        filmStorage.checkContainsFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.checkContainsFilmById(filmId);
        userStorage.checkContainsUserById(userId);
        filmStorage.getFilmByIdFromStorage(filmId).addLike(userId);
        log.debug("Фильму с id = {} поставлен Лайк пользователем id = {}", filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.checkContainsFilmById(filmId);
        userStorage.checkContainsUserById(userId);
        filmStorage.getFilmByIdFromStorage(filmId).removeLike(userId);
        log.debug("У фильма id = {} удален Лайк пользователем id = {}", filmId, userId);
    }

    public List<Film> returnMostPopularFilms(Long count) {
        return filmStorage.getAllFilmsFromStorage().stream()
                .sorted((x, y)
                        -> Integer.compare(y.getLikes().size(), x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void isCorrectFilm(Film film) throws ValidationException {
        LocalDate birthdayMovie = LocalDate.of(1895, 12, 28);
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно быть не более 200 символов");
        } else if (film.getReleaseDate().isBefore(birthdayMovie)) {
            throw new ValidationException(String.format("Дата релиза указана не корректно: %s", film.getReleaseDate()));
        }
    }
}
