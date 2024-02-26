package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.dto.ProfileDTO;
import ru.job4j.site.service.ProfilesService;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CheckDev пробное собеседование
 * ProfilesControllerTest тесты на контроллер IndexController
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.09.2023
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProfilesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfilesService profilesService;

    @Test
    void whenGetProfileByIdThenReturnPageProfileView() throws Exception {
        var id = 1;
        var profile = new ProfileDTO(id, "username", "experience", 1,
                Calendar.getInstance(), Calendar.getInstance());
        when(this.profilesService.getProfileById(id)).thenReturn(Optional.of(profile));
        this.mockMvc.perform(get("/profiles/{id}", profile.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("profile", profile))
                .andExpect(view().name("/profiles/profileView"));
    }

    @Test
    void whenGetAllProfilesThenReturnPageProfiles() throws Exception {
        var profile1 = new ProfileDTO(1, "username1", "experience1", 1, Calendar.getInstance(), Calendar.getInstance());
        var profile2 = new ProfileDTO(2, "username2", "experience2", 2,
                Calendar.getInstance(), Calendar.getInstance());
        var listProfile = List.of(profile1, profile2);
        when(profilesService.getAllProfile()).thenReturn(listProfile);
        this.mockMvc.perform(get("/profiles/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("profiles", listProfile))
                .andExpect(view().name("profiles/profiles"));
    }
}