package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserService(UserStorage userStorage, FriendshipDao friendshipDao) {
        this.userStorage = userStorage;
        this.friendshipDao = friendshipDao;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsersFromStorage();
    }

    public User getUserById(Long id) {
        return userStorage.getUserByIdFromStorage(id).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Пользователь с переданным id = %d не найден.", id)));
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
        friendshipDao.addFriend(id, friendId);
        log.debug("Пользователь id {} отправил заявку в друзья пользователю {} ", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        friendshipDao.checkAvailabilityFriendshipInList(id, friendId);
        friendshipDao.removeAvailabilityFriendshipFromList(id, friendId);
        log.debug("Пользователи с id {} и {} удалены из друзей", id, friendId);
    }

    public List<User> getFriendsById(Long id) {
        userStorage.checkContainsUserById(id);
        return friendshipDao.getAllFriends(id);
    }

    public List<User> getAllMutualFriendsById(Long id, Long otherId) {
        userStorage.checkContainsUserById(id);
        userStorage.checkContainsUserById(otherId);
        return friendshipDao.getAllMutualFriends(id, otherId);
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
