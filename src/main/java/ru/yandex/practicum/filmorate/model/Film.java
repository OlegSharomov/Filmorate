package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class Film {
    private long id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    @Min(1)
    private final Integer duration; //в минутах
    @Min(value = 0)
    private Float rate;
    private final Set<Long> likes = new HashSet<>();    // содержит id пользователей, которые поставили лайки

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long idFriend) {
        likes.remove(idFriend);
    }
}