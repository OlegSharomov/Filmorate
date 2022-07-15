package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Builder
@Data
public class Film {
    private static int countId = 0;

    private final int id = setIdOfFilm();
    @NonNull
    private final String name;
    @NonNull
    private final String description;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    private final Duration duration;

    public int setIdOfFilm() {
        return ++countId;
    }
}