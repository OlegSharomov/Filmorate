package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    // возвращает список всех пользователей
    List<User> getAllUsersFromStorage();

    // возвращает пользователя по id
    User getUserByIdFromStorage(Long id);

    // создает нового пользователя в хранилище и возвращает его в ответе
    User createUser(User user);

    // изменяет пользователя и возвращает его в ответе
    User updateUser(User user);

    //проверяет наличие в мапе пользователя по id
    void checkContainsUserById(Long userId);
}
