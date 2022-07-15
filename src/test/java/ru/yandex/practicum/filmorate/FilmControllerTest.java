package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    Film film = Film.builder()
            .name("Matrix")
            .description("The best movie")
            .releaseDate(LocalDate.of(1999, 3, 31))
            .duration(Duration.ofMinutes(136))
            .build();
    @Autowired
    private FilmController controller;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void contextLoads2() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListOfFilms() throws Exception {
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnFilm() throws Exception {
        String jacksonFilm = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films")
                        .content(jacksonFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(film))));
    }

    @Test
    public void shouldCreateAndReturnMatrixMovie() throws Exception {
        String jacksonFilm = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films")
                        .content(jacksonFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(jacksonFilm));
    }

    @Test
    public void shouldReturnCode400WithFailName() throws Exception {
        String filmWithFailName = "{" +
                "\"name\":\" \"," +
                "\"description\":\"The best movie\"," +
                "\"releaseDate\":\"1999-03-31\"," +
                "\"duration\":\"PT2H16M\"}";
        this.mockMvc.perform(post("/films")
                        .content(filmWithFailName).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnCode400WithFailDescription() throws Exception {
        String filmWithFailDescription = "{" +
                "\"name\":\"Matrix\"," +
                "\"description\":\" \"," +
                "\"releaseDate\":\"1999-03-31\"," +
                "\"duration\":\"PT2H16M\"}";
        this.mockMvc.perform(post("/films").
                        content(filmWithFailDescription).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnCode400WithFailReleaseDate() throws Exception {
        String filmWithFailReleaseDate = "{" +
                "\"name\":\"Matrix\"," +
                "\"description\":\"The best movie\"," +
                "\"releaseDate\":\"1895-12-27\"," +
                "\"duration\":\"PT2H16M\"}";
        this.mockMvc.perform(post("/films")
                        .content(filmWithFailReleaseDate).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnCode400WithFailDuration() throws Exception {
        String filmWithFailDuration = "{" +
                "\"name\":\"Matrix\"," +
                "\"description\":\"The best movie\"," +
                "\"releaseDate\":\"1999-03-31\"," +
                "\"duration\":\"-PT1M\"}";
        this.mockMvc.perform(post("/films")
                        .content(filmWithFailDuration).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldChangeFilm() throws Exception {
        String jacksonFilm = mapper.writeValueAsString(film);
        System.out.println(jacksonFilm);
        this.mockMvc.perform(post("/films").content(jacksonFilm).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        String updatedFilm = "{\"id\":1," +
                "\"name\":\"Matrix\"," +
                "\"description\":\"The best movie in the world\"," +
                "\"releaseDate\":\"1999-03-30\"," +
                "\"duration\":\"PT2H10M\"}";
        this.mockMvc.perform(put("/films").content(updatedFilm).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(updatedFilm));
    }
}
