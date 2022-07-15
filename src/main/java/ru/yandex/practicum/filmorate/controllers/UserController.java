package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> showAllUsers() {
        log.debug("Получен запрос к эндпоинту GET /users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.debug("Получен запрос к эндпоинту POST /users с переданным телом: '{}'", user);
        try {
            if (users.containsKey(user.getId())) {
                throw new ValidationException("Пользователь с данным id уже существует");
            }
            if (isCorrectUser(user)) {
                users.put(user.getId(), user);
                log.debug("Пользователь успешно добавлен");

            }
        } catch (NullPointerException | ValidationException ex) {
            log.warn("При обработке запроса к эндпоинту POST /users произошла ошибка: '{}'", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Получен запрос к эндпоинту PUT /users с переданным телом: '{}'", user);
        try {
            if (isCorrectUser(user)) {
                users.put(user.getId(), user);
                log.debug("Пользователь успешно обновлен");
            }
        } catch (NullPointerException | ValidationException ex) {
            log.warn("При обработке запроса к эндпоинту PUT /users произошла ошибка: '{}'", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return user;
    }

    private boolean isCorrectUser(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта введена не корректно");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин введен не корректно");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения введена не корректно");
        } else if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
    }
}
