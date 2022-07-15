package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
public class GenresControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnAllGenres() throws Exception {
        this.mockMvc.perform(get("/genres"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Комедия"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Драма"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[2].name").value("Мультфильм"))
                .andExpect(jsonPath("$[3].id").value("4"))
                .andExpect(jsonPath("$[3].name").value("Триллер"))
                .andExpect(jsonPath("$[4].id").value("5"))
                .andExpect(jsonPath("$[4].name").value("Документальный"))
                .andExpect(jsonPath("$[5].id").value("6"))
                .andExpect(jsonPath("$[5].name").value("Боевик"));
    }

    @Test
    public void shouldGetGenreById() throws Exception {
        this.mockMvc.perform(get("/genres/{id}", 3))
                .andExpect(jsonPath("$.name").value("Мультфильм"));
    }

    @Test
    public void shouldReturnCode400WithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/genres/{id}", 7))
                .andExpect(status().is4xxClientError());
    }
}
