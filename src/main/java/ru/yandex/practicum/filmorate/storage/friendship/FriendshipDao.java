package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {
    void addFriend(Long id, Long friendId);

    void removeAvailabilityFriendshipFromList(Long id, Long friendId);

    void checkAvailabilityFriendshipInList(Long id, Long friendId);

    List<User> getAllFriends(Long id);

    List<User> getAllMutualFriends(Long id, Long otherId);
}
