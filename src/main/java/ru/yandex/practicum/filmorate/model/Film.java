package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Builder
@Data
public class Film {
    private Long id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    @Min(1)
    private final Integer duration; //в минутах
    private int rate;
    @NotNull
    private Mpa mpa;
    private TreeSet<Genre> genres;
    // исправить логику
    private Set<Long> likes;    // содержит id пользователей, которые поставили лайки

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long idFriend) {
        likes.remove(idFriend);
    }
}