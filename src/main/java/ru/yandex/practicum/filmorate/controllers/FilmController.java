package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // GET /films - возвращает список всех фильмов
    @GetMapping
    public List<Film> showAllFilms() {
        log.debug("Получен запрос к эндпоинту GET /films");
        return filmService.getAllFilms();
    }

    // GET /films/{id} - возвращает фильм по переменной пути id
    @GetMapping("/{id}")
    public Film showFilmById(@PathVariable Long id) {
        checkFilmId(id);
        log.debug("Получен запрос к эндпоинту GET /films/{id}, в котором переменная пути: id = {}", id);
        return filmService.getFilmById(id);
    }

    // POST /films - создает новый фильм и возвращает его
    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос к эндпоинту POST /films с переданным телом: '{}'", film);
        return filmService.checkAndCreateFilm(film);
    }

    // PUT /films - изменяет фильм и возвращает его
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос к эндпоинту PUT /films с переданным телом: '{}'", film);
        return filmService.checkAndUpdateFilm(film);
    }

    // PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        log.debug("Получен запрос к эндпоинту PUT /films/{id}/like/{userId}. " +
                "В котором переменные пути: id = {}, userId = {}", id, userId);
        checkFilmId(id);
        checkUserId(userId);
        filmService.addLike(id, userId);
    }

    // DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.debug("Получен запрос к эндпоинту DELETE /films/{id}/like/{userId}. " +
                "В котором переменные пути: id = {}, userId = {}", id, userId);
        checkFilmId(id);
        checkUserId(userId);
        filmService.removeLike(id, userId);
    }

    /* GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     Если значение параметра count не задано, верните первые 10. */
    @GetMapping("/popular")
    public List<Film> showFilmsByLikes(@RequestParam(defaultValue = "10", required = false) Long count) {
        log.debug("Получен запрос к эндпоинту GET /films/popular. " +
                "С параметром запроса: count = {}", count);
        return filmService.returnMostPopularFilms(count);
    }

    private void checkFilmId(Long id) {
        if (id == null) {
            throw new ValidationException(String.format("Не корректно введен id фильма. Id = %s", id));
        }
    }

    private void checkUserId(Long userId) {
        if (userId == null) {
            throw new ValidationException(String.format("Не корректно передан id пользователя. Id = %s", userId));
        }
    }
}
