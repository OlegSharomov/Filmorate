package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsersFromStorage() {
        String sql = "SELECT user_id, user_name, user_login, user_email, user_birthday FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public Optional<User> getUserByIdFromStorage(Long id) {
        String sql = "SELECT user_id, user_name, user_login, user_email, user_birthday FROM users WHERE user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            User user = User.builder()
                    .id(rs.getLong("user_id"))
                    .name(Objects.requireNonNull(rs.getString("user_name")))
                    .login(rs.getString("user_login"))
                    .email(rs.getString("user_email"))
                    .birthday(Objects.requireNonNull(rs.getDate("user_birthday")).toLocalDate())
                    .build();
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @SneakyThrows
    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (user_name, user_login, user_email, user_birthday) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET user_name = ?, user_login = ?, user_email = ?, " +
                "user_birthday = ? WHERE user_id = ? ;";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void checkContainsUserById(Long userId) {
        String sql = "SELECT user_login FROM users WHERE user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
        if (!rs.next()) {
            throw new ObjectNotFoundException(String.format("Пользователь с переданным id = %d не найден.", userId));
        }
    }
}
