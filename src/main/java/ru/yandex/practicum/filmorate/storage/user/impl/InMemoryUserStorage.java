package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private long countId = 0;
    private final Map<Long, User> users = new HashMap<>();

    private Long setIdOfUser() {
        return ++countId;
    }

    @Override
    public List<User> getAllUsersFromStorage() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserByIdFromStorage(Long id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(setIdOfUser());
        users.put(user.getId(), user);
        log.debug(String.format("Пользователь %s успешно добавлен", user));
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.debug(String.format("Пользователь %s успешно обновлен", user));
        return user;
    }

    @Override
    public void checkContainsUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с переданным id = %d не найден.", userId));
        }
    }
}
