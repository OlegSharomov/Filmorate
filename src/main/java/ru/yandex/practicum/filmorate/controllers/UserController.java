package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users - возвращает список всех пользователей
    @GetMapping
    public List<User> showAllUsers() {
        log.debug("Получен запрос к эндпоинту GET /users");
        return userService.getAllUsers();
    }

    // GET /users/{id} - возвращает пользователя по переменной пути id
    @GetMapping("/{id}")
    public User showUserById(@PathVariable Long id) {
        log.debug("Получен запрос к эндпоинту GET /users/{id}, в котором переменная пути: id = {}", id);
        checkPathVariableId(id);
        return userService.getUserById(id);
    }

    // POST /users - создает нового пользователя и возвращает его
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос к эндпоинту POST /users с переданным телом: '{}'", user);
        return userService.checkAndCreateUser(user);
    }

    // PUT /users - изменяет пользователя и возвращает его
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос к эндпоинту PUT /users с переданным телом: '{}'", user);
        return userService.checkAndUpdateUser(user);
    }

    //PUT /users/{id}/friends/{friendId} — добавление в друзья.
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.debug("Получен запрос к эндпоинту PUT /users/{id}/friends/{friendId}, " +
                "в котором переменные пути: id = {}, friendId = {}", id, friendId);
        checkPathVariableId(id);
        checkPathVariableFriendId(friendId);
        userService.checkAndAddFriend(id, friendId);
    }

    // DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Long id,
                              @PathVariable Long friendId) {
        log.debug("Получен запрос к эндпоинту DELETE /users/{id}/friends/{friendId}, " +
                "в котором переменные пути: id = {}, friendId = {}", id, friendId);
        checkPathVariableId(id);
        checkPathVariableFriendId(friendId);
        userService.removeFriend(id, friendId);
    }

    // GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public List<User> showAllFriends(@PathVariable Long id) {
        log.debug("Получен запрос к эндпоинту GET /users/{id}/friends, " +
                "в котором переменная пути: id = {}", id);
        checkPathVariableId(id);
        return userService.getFriendsById(id);
    }

    // GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showAllMutualFriendsById(@PathVariable Long id,
                                               @PathVariable Long otherId) {
        log.debug("Получен запрос к эндпоинту GET /users/{id}/friends/common/{otherId}, " +
                "в котором переменные пути: id = {}, otherId = {}", id, otherId);
        checkPathVariableId(id);
        if (otherId == null) {
            throw new ValidationException(String.format("Не корректно введен id другого пользователя. otherId = %s", otherId));
        }
        return userService.getAllMutualFriendsById(id, otherId);
    }

    private void checkPathVariableId(Long id) {
        if (id == null) {
            throw new ValidationException(String.format("Не корректно введен id пользователя. Id = %s", id));
        }
    }

    private void checkPathVariableFriendId(Long friendId) {
        if (friendId == null) {
            throw new ValidationException(String.format("Не корректно введен параметр friendId. friendId = %s", friendId));
        }
    }
}
