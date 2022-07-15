package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class User {
    private long id;
    @Email
    private final String email;
    @NotBlank
    private String login;
    @NonNull
    private String name;
    @NonNull
    private LocalDate birthday;
    final private Set<Long> friends = new HashSet<>();

    public void addFriend(Long idFriend) {
        friends.add(idFriend);
    }

    public void removeFriend(Long idFriend) {
        friends.remove(idFriend);
    }
}
