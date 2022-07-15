package ru.yandex.practicum.filmorate.storage.friendship.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDao;

import java.util.List;

@Slf4j
@Repository
public class FriendshipDaoImpl implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sql = "INSERT INTO friendship_list VALUES (?, ?, 'unconfirmed')";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void removeAvailabilityFriendshipFromList(Long id, Long friendId) {
        String sql = "DELETE FROM friendship_list WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }


    @Override
    public void checkAvailabilityFriendshipInList(Long id, Long friendId) {
        String sql = "SELECT user_id, friend_id FROM friendship_list WHERE user_id = ? AND friend_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id, friendId);
        if (!rs.next()) {
            throw new ObjectNotFoundException(String.format("Дружеские отношения пользователя id = %d и " +
                    "friendId = %d не найдены .", id, friendId));
        }
    }

    @Override
    public List<User> getAllFriends(Long id) {
        String sql = "SELECT user_id, user_name, user_login, user_email, user_birthday FROM users " +
                "WHERE user_id IN (SELECT friend_id FROM friendship_list WHERE user_id = ?);";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public List<User> getAllMutualFriends(Long id, Long otherId) {
        String sql = "SELECT user_id, user_name, user_login, user_email, user_birthday FROM users " +
                "WHERE user_id IN (SELECT friend_id FROM friendship_list WHERE user_id = ? " +
                "AND friend_id IN (SELECT friend_id FROM friendship_list WHERE user_id = ?));";
        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }

}
