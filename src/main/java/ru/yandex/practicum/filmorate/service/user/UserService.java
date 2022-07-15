package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsersFromStorage();
    }

    public User getUserById(Long id) {
        userStorage.checkContainsUserById(id);
        return userStorage.getUserByIdFromStorage(id);
    }

    public User checkAndCreateUser(User user) {
        isCorrectUser(user);
        return userStorage.createUser(user);
    }

    public User checkAndUpdateUser(User user) {
        isCorrectUser(user);
        userStorage.checkContainsUserById(user.getId());
        return userStorage.updateUser(user);
    }

    public void checkAndAddFriend(Long id, Long friendId) {
        userStorage.checkContainsUserById(id);
        userStorage.checkContainsUserById(friendId);
        userStorage.getUserByIdFromStorage(id).addFriend(friendId);
        userStorage.getUserByIdFromStorage(friendId).addFriend(id);
        log.debug("Пользователи с id {} и {} добавлены в друзья", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userStorage.checkContainsUserById(id);
        userStorage.checkContainsUserById(friendId);
        if (!userStorage.getUserByIdFromStorage(id).getFriends().contains(friendId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с переданным friendId (%s) " +
                    "не найден в друзьях пользователя id (%s)", friendId, id));
        }
        if (!userStorage.getUserByIdFromStorage(friendId).getFriends().contains(id)) {
            throw new ObjectNotFoundException(String.format("Пользователь с переданным id (%s) " +
                    "не найден в друзьях пользователя friendId (%s)", id, friendId));
        }
        userStorage.getUserByIdFromStorage(id).removeFriend(friendId);
        userStorage.getUserByIdFromStorage(friendId).removeFriend(id);
        log.debug("Пользователи с id {} и {} удалены из друзей", id, friendId);
    }

    public List<User> getFriendsById(Long id) {
        userStorage.checkContainsUserById(id);
        return userStorage.getUserByIdFromStorage(id).getFriends().stream()
                .map(userStorage::getUserByIdFromStorage)
                .collect(Collectors.toList());
    }

    public List<User> getAllMutualFriendsById(Long id, Long otherId) {
        userStorage.checkContainsUserById(id);
        userStorage.checkContainsUserById(otherId);
        Set<Long> friendsId = userStorage.getUserByIdFromStorage(id).getFriends();
        Set<Long> friendsOtherId = userStorage.getUserByIdFromStorage(otherId).getFriends();
        return friendsId.stream()
                .filter(friendsOtherId::contains)
                .map(userStorage::getUserByIdFromStorage)
                .collect(Collectors.toList());
    }

    private void isCorrectUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержить пробелы: " + user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения введена не корректно: " + user.getBirthday());
        } else if (user.getName().isBlank()) {
            log.info("Поле имени пользователя было пустое. Оно изменено на: " + user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
