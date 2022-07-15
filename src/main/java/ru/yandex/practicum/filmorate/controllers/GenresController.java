package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genres.GenresService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenresService genresService;

    @Autowired
    public GenresController(GenresService genresService) {
        this.genresService = genresService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genresService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable @NotNull Integer id) {
        log.debug("Получен запрос к эндпоинту GET /genres/{id}, в котором переменная пути: id = {}", id);
        return genresService.getGenreById(id);
    }
}
