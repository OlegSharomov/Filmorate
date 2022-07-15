package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@Sql(scripts = {"/schemaForTests.sql", "/dataForTests.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {
    User user = User.builder()
            .id(1L)
            .email("nameEmail@yandex.ru")
            .login("funnyMan")
            .name("Ivan")
            .birthday(LocalDate.of(2000, 10, 10)).build();

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldReturnEmptyListOfUsers() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUser() throws Exception {
        String jacksonUser = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(jacksonUser).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(user))));
    }

    @Test
    public void shouldCreateAndReturnUser() throws Exception {
        String jacksonUser = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(jacksonUser).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(jacksonUser));
    }

    @Test
    public void shouldReturnCode400WithFailLogin() throws Exception {
        String userWithFailLogin = "{" +
                "\"email\":\"nameEmail@yandex.ru\"," +
                "\"login\":\"funny Man\"," +
                "\"name\":\"Ivan\"," +
                "\"birthday\":\"2000-10-10\"}";
        this.mockMvc.perform(post("/users").content(userWithFailLogin).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnCode400WithFailName() throws Exception {
        String userWithFailName = "{" +
                "\"email\":\"nameEmail@yandex.ru\"," +
                "\"login\":\"funnyMan\"," +
                "\"birthday\":\"2000-10-10\"}";
        this.mockMvc.perform(post("/users").content(userWithFailName).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnCode400WithFailEmail() throws Exception {
        String userWithFailEmail = "{" +
                "\"email\":\"nameEmailYandex.ru\"," +
                "\"login\":\"funnyMan\"," +
                "\"name\":\" \"," +
                "\"birthday\":\"2000-10-10\"}";
        this.mockMvc.perform(post("/users").content(userWithFailEmail).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnCode400WithFailBirthday() throws Exception {
        String userWithFailBirthday = "{" +
                "\"email\":\"nameEmail@yandex.ru\"," +
                "\"login\":\"funnyMan\"," +
                "\"name\":\"Ivan\"," +
                "\"birthday\":\"2446-10-10\"}";
        this.mockMvc.perform(post("/users").content(userWithFailBirthday).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnStatus200AndLoginInsteadOfName() throws Exception {
        String userWithoutName = "{" +
                "\"email\":\"nameEmail@yandex.ru\"," +
                "\"login\":\"funnyMan\"," +
                "\"name\":\" \"," +
                "\"birthday\":\"2000-10-10\"}";
        this.mockMvc.perform(post("/users").content(userWithoutName).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"funnyMan\"")));
    }
}
