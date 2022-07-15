package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDao;
import ru.yandex.practicum.filmorate.storage.likes.LikesDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenreDao filmGenreDao;
    private final LikesDao likesDao;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage,
                       FilmGenreDao filmGenreDao, LikesDao likesDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmGenreDao = filmGenreDao;
        this.likesDao = likesDao;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilmsFromStorage();
    }

    public Film getFilmById(Long id) {
        Optional<Film> filmOptional = filmStorage.getFilmByIdFromStorage(id);
        if (filmOptional.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Фильм с переданным id = %d не найден.", id));
        } else {
            Film filmForAnswer = filmOptional.get();
            TreeSet<Genre> genres = filmGenreDao.getAllGenresByFilmId(id);
            if (!genres.isEmpty()) {
                filmForAnswer.setGenres(genres);
            }
            return filmForAnswer;
        }
    }

    public Film checkAndCreateFilm(Film film) {
        isCorrectFilm(film);
        Film filmForAnswer = filmStorage.addNewFilm(film);          // положили фильм в таблицу и вернули фильм с id
        if (film.getGenres() != null) {
            putGenresInTableFilmGenre(film, filmForAnswer.getId());     // кладем жанры в таблицу film_genre
            TreeSet<Genre> genres = filmGenreDao.getAllGenresByFilmId(filmForAnswer.getId());// получаем объекты жанров
            filmForAnswer.setGenres(genres);                                             // собираем фильм для ответа
        }
        return filmForAnswer;
    }

    public Film checkAndUpdateFilm(Film film) {
        isCorrectFilm(film);
        filmStorage.checkContainsFilmById(film.getId());
        Film filmForAnswer = filmStorage.updateFilm(film);              // обновили фильм в таблице и вернули его
        filmGenreDao.removeAllGenresByIdFilm(film.getId());         // удалили все записи из film_genre этого фильма
        if (film.getGenres() != null) {
            putGenresInTableFilmGenre(film, film.getId());          // кладем жанры в таблицу film_genre
            TreeSet<Genre> genres = filmGenreDao.getAllGenresByFilmId(film.getId());    // получаем объекты жанров
            filmForAnswer.setGenres(genres);                                        // собираем фильм для ответа
        }
        return filmForAnswer;
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.checkContainsFilmById(filmId);
        userStorage.checkContainsUserById(userId);
        likesDao.addLike(filmId, userId);
        log.debug("Фильму с id = {} поставлен Лайк пользователем id = {}", filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.checkContainsFilmById(filmId);
        userStorage.checkContainsUserById(userId);
        likesDao.removeLike(filmId, userId);
        log.debug("У фильма id = {} удален Лайк пользователем id = {}", filmId, userId);
    }

    public List<Film> returnMostPopularFilms(Long count) {
        return likesDao.getMostPopularFilms(count);
    }

    private void putGenresInTableFilmGenre(Film film, Long filmId) {
        film.getGenres().stream()
                .filter(x -> x.getId() != null)
                .forEach(x -> filmGenreDao.addGenre(filmId, x.getId()));
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
