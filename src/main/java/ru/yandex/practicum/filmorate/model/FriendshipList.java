package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FriendshipList {
    @NotNull private final User user;
    @NotNull private final User friend;
    @NotNull private String status;

    public FriendshipList(User user, User friend, String status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }
}
