package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
@Data
public class User {
    private static int countId = 0;

    private final int id = setIdOfUser();
    @NonNull
    private final String email;
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    private LocalDate birthday;

    public int setIdOfUser() {
        return ++countId;
    }
}
