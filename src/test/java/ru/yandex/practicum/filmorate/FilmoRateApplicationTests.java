package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesDao;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    User userTemplate1 = User.builder()
            .email("name1Email@yandex.ru")
            .login("funnyMan")
            .name("Ivan")
            .birthday(LocalDate.of(2000, 10, 10))
            .build();
    User userTemplate2 = User.builder()
            .email("name2Email@yandex.ru")
            .login("Albert")
            .name("Albert")
            .birthday(LocalDate.of(1988, 1, 1))
            .build();
    Film filmTemplate1 = Film.builder()
            .name("Matrix")
            .description("Follow the white rabbit")
            .releaseDate(LocalDate.of(1999, 3, 31))
            .duration(136)
            .mpa(new Mpa(4, null))
            .genres(new TreeSet<>(Collections.singleton(new Genre(6, null))))
            .build();

    Film filmTemplate2 = Film.builder()
            .name("Мальчик в полосатой пижаме")
            .description("Они ходят в пижамах, играют в номера, которые пришиты к пижамам, " +
                    "и зачем-то сжигают старую одежду в больших печах")
            .releaseDate(LocalDate.of(2008, 9, 12))
            .duration(94)
            .mpa(new Mpa(5, null))
            .genres(new TreeSet<>(Set.of(new Genre(2, null), new Genre(4, null))))
            .build();

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private final UserDbStorage userStorage;
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final FilmService filmService;
    @Autowired
    private final LikesDao likesDao;

    //UserDbStorage
    @Test
    public void testFindUserById() {
        User userAfterCreation1 = userStorage.createUser(userTemplate1);
        User userAfterCreation2 = userStorage.createUser(userTemplate2);
        Optional<User> userOptional1 = userStorage.getUserByIdFromStorage(userAfterCreation1.getId());
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", userAfterCreation1.getId())
                                .hasFieldOrPropertyWithValue("name", "Ivan"));
        Optional<User> userOptional2 = userStorage.getUserByIdFromStorage(userAfterCreation2.getId());
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", userAfterCreation2.getId())
                                .hasFieldOrPropertyWithValue("name", "Albert"));
        List<User> users = userStorage.getAllUsersFromStorage();
        assertThat(users).contains(userTemplate1, userTemplate2);
        removeUserFromStorage(userOptional1.get().getId());
        removeUserFromStorage(userOptional2.get().getId());
    }

    @Test
    public void testFindUserByIncorrectId() {
        Optional<User> userOptional1 = userStorage.getUserByIdFromStorage(999_999L);
        assertThat(userOptional1).isEmpty();
    }

    @Test
    public void testReturnCorrectUserId() {
        User userTemplate3 = User.builder()
                .id(999_999L)
                .email("name1Email@yandex.ru")
                .login("funnyMan")
                .name("Ivan")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        User userAfterCreation1 = userStorage.createUser(userTemplate3);      // userTemplate3 with id=999
        Optional<User> userOptional1 = userStorage.getUserByIdFromStorage(userAfterCreation1.getId());
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", userAfterCreation1.getId())
                                .hasFieldOrPropertyWithValue("name", "Ivan"));
        assertThat(userOptional1.get().getId() != 999_999L);
        removeUserFromStorage(userOptional1.get().getId());
    }

    @Test
    public void testUpdateUser() {
        User userAfterCreation1 = userStorage.createUser(userTemplate1);
        User updateUser = User.builder()
                .id(userAfterCreation1.getId())
                .email("updateName1Email@yandex.ru")
                .login("notSoFunnyMan")
                .name("Vanya")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userStorage.updateUser(updateUser);
        Optional<User> userOptional1 = userStorage.getUserByIdFromStorage(userAfterCreation1.getId());
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", userAfterCreation1.getId())
                                .hasFieldOrPropertyWithValue("email", "updateName1Email@yandex.ru")
                                .hasFieldOrPropertyWithValue("login", "notSoFunnyMan")
                                .hasFieldOrPropertyWithValue("name", "Vanya"));
        removeUserFromStorage(userOptional1.get().getId());
    }

    // FilmDbStorage
    @Test
    public void testFindFilmById() {
        Film filmAfterCreation1 = filmService.checkAndCreateFilm(filmTemplate1);
        Film filmAfterCreation2 = filmService.checkAndCreateFilm(filmTemplate2);
        Optional<Film> filmOptional1 = filmStorage.getFilmByIdFromStorage(filmAfterCreation1.getId());
        assertThat(filmOptional1).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", filmAfterCreation1.getId())
                                .hasFieldOrPropertyWithValue("name", "Matrix")
                                .hasFieldOrPropertyWithValue("mpa", new Mpa(4, "R")));
        Optional<Film> filmOptional2 = filmStorage.getFilmByIdFromStorage(filmAfterCreation2.getId());
        assertThat(filmOptional2).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", filmAfterCreation2.getId())
                                .hasFieldOrPropertyWithValue("name", "Мальчик в полосатой пижаме")
                                .hasFieldOrPropertyWithValue("mpa", new Mpa(5, "NC-17")));
        List<Film> films = filmStorage.getAllFilmsFromStorage();
        assertThat(films).contains(filmOptional1.get(), filmOptional2.get());
        assertThat(filmService.getFilmById(filmAfterCreation1.getId()))
                .hasFieldOrPropertyWithValue("genres",
                        new TreeSet<>(Collections.singleton(new Genre(6, "Боевик"))));
        assertThat(filmService.getFilmById(filmAfterCreation2.getId()))
                .hasFieldOrPropertyWithValue("genres",
                        new TreeSet<>(Set.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))));
        removeFilmFromStorage(filmOptional1.get().getId());
        removeFilmFromStorage(filmOptional2.get().getId());
    }

    @Test
    public void testFindFilmByIncorrectId() {
        Optional<Film> filmOptional1 = filmStorage.getFilmByIdFromStorage(999_999L);
        assertThat(filmOptional1).isEmpty();
    }

    @Test
    public void testReturnCorrectFilmId() {
        Film filmTemplate3 = Film.builder()
                .id(999_999L)
                .name("Matrix")
                .description("Follow the white rabbit")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .mpa(new Mpa(4, null))
                .build();
        Film filmAfterCreation1 = filmStorage.addNewFilm(filmTemplate3);
        Optional<Film> filmOptional1 = filmStorage.getFilmByIdFromStorage(filmAfterCreation1.getId());
        assertThat(filmOptional1).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", filmAfterCreation1.getId())
                                .hasFieldOrPropertyWithValue("name", "Matrix"));
        assertThat(filmOptional1.get().getId() != 999_999L);
        removeFilmFromStorage(filmOptional1.get().getId());
    }

    @Test
    public void testUpdateFilm() {
        Film filmAfterCreation1 = filmService.checkAndCreateFilm(filmTemplate1);
        Film updateFilm = Film.builder()
                .id(filmAfterCreation1.getId())
                .name("Matrix Update")
                .description("He is the chosen one")
                .releaseDate(LocalDate.of(1999, 3, 30))
                .duration(140)
                .mpa(new Mpa(5, null))
                .genres(new TreeSet<>(Set.of(new Genre(6, null), new Genre(4, null))))
                .build();
        filmService.checkAndUpdateFilm(updateFilm);
        Film filmAfterUpdate1 = filmService.getFilmById(filmAfterCreation1.getId());
        assertThat(filmAfterUpdate1)
                .hasFieldOrPropertyWithValue("id", filmAfterCreation1.getId())
                .hasFieldOrPropertyWithValue("name", "Matrix Update")
                .hasFieldOrPropertyWithValue("description", "He is the chosen one")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 3, 30))
                .hasFieldOrPropertyWithValue("mpa", new Mpa(5, "NC-17"))
                .hasFieldOrPropertyWithValue("genres",
                        new TreeSet<>(Set.of(new Genre(6, "Боевик"), new Genre(4, "Триллер"))));
        removeFilmFromStorage(filmAfterUpdate1.getId());
    }

    @Test
    public void testLikes() {
        User user1 = userStorage.createUser(userTemplate1);
        User user2 = userStorage.createUser(userTemplate2);
        Film filmAfterCreations1 = filmService.checkAndCreateFilm(filmTemplate1);
        Film filmAfterCreations2 = filmService.checkAndCreateFilm(filmTemplate2);
        Film film1 = filmService.getFilmById(filmAfterCreations1.getId());
        Film film2 = filmService.getFilmById(filmAfterCreations2.getId());
        likesDao.addLike(film1.getId(), user1.getId());
        likesDao.addLike(film2.getId(), user1.getId());
        likesDao.addLike(film2.getId(), user2.getId());
        List<Film> popFilms1 = likesDao.getMostPopularFilms(2L);
        assertThat(popFilms1).hasSize(2);
        assertThat(popFilms1.get(0)).hasFieldOrPropertyWithValue("name", "Мальчик в полосатой пижаме");
        assertThat(popFilms1.get(1)).hasFieldOrPropertyWithValue("name", "Matrix");
        likesDao.removeLike(film2.getId(), user1.getId());
        likesDao.removeLike(film2.getId(), user2.getId());
        List<Film> popFilms2 = likesDao.getMostPopularFilms(2L);
        assertThat(popFilms2).hasSize(2);
        assertThat(popFilms2.get(0)).hasFieldOrPropertyWithValue("name", "Matrix");
        assertThat(popFilms2.get(1)).hasFieldOrPropertyWithValue("name", "Мальчик в полосатой пижаме");
    }

    private void removeUserFromStorage(Long userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private void removeFilmFromStorage(Long filmId) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
