package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Genre implements Comparable<Genre> {
    private final Integer id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return this.id.compareTo(o.id);
    }
}
