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
public class MpaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnAllMpa() throws Exception {
        this.mockMvc.perform(get("/mpa"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("G"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("PG"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[2].name").value("PG-13"))
                .andExpect(jsonPath("$[3].id").value("4"))
                .andExpect(jsonPath("$[3].name").value("R"))
                .andExpect(jsonPath("$[4].id").value("5"))
                .andExpect(jsonPath("$[4].name").value("NC-17"));
    }

    @Test
    public void shouldGetMpaById() throws Exception {
        this.mockMvc.perform(get("/mpa/{id}", 3))
                .andExpect(jsonPath("$.name").value("PG-13"));
    }

    @Test
    public void shouldReturnCode400WithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/mpa/{id}", 6))
                .andExpect(status().is4xxClientError());
    }
}
