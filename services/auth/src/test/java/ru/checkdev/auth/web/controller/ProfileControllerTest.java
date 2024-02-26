package ru.checkdev.auth.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.auth.dto.ProfileDTO;
import ru.checkdev.auth.service.ProfileService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CheckDev пробное собеседование
 * ProfileControllerTest тестирование RestController для отправки модели ProfileDTO.
 *
 * @author Dmitry Stepanov
 * @version 01:45
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {
    @MockBean
    private ProfileService profileService;
    @Autowired
    private MockMvc mockMvc;
    private ProfileController profileController;
    private final ProfileDTO profileDTO1 = new ProfileDTO(
            1, "name1", "experience1", 1, null, null);
    private final ProfileDTO profileDTO2 = new ProfileDTO(
            2, "name2", "experience2", 2, null, null);


    @Before
    public void initController() {
        this.profileController = new ProfileController(profileService);
    }

    @Test
    @WithMockUser
    public void whenGetProfileByIdThenReturnStatusOK() throws Exception {
        when(profileService.findProfileByID(profileDTO1.getId())).thenReturn(Optional.of(profileDTO1));
        mockMvc.perform(get("/profiles/{id}", profileDTO1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(profileDTO1.getId()))
                .andExpect(jsonPath("$.username").value(profileDTO1.getUsername()))
                .andExpect(jsonPath("$.experience").value(profileDTO1.getExperience()))
                .andExpect(jsonPath("$.photoId").value(profileDTO1.getPhotoId()))
                .andDo(print());
    }

    @SuppressWarnings("checkstyle:OperatorWrap")
    @Test
    @WithMockUser
    public void whenGetProfileByIdProfileNotFoundThenReturnStatusNotFound() throws Exception {
        when(profileService.findProfileByID(profileDTO1.getId())).thenReturn(Optional.empty());
        mockMvc.perform(get("/profiles/{id}/", anyInt()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void whenGetAllProfilesOrderByCreateDescThenReturnStatusOkAndBody() throws Exception {
        var profiles = List.of(profileDTO1, profileDTO2);
        when(profileService.findProfilesOrderByCreatedDesc()).thenReturn(profiles);
        mockMvc.perform(get("/profiles/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(profiles.size()))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void whenGetAllProfilesOrderByCreateDescListEmptyThenReturnStatusNoContent() throws Exception {
        when(profileService.findProfilesOrderByCreatedDesc()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/profiles/"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}