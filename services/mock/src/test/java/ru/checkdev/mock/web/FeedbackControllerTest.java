package ru.checkdev.mock.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.dto.FeedbackDTO;
import ru.checkdev.mock.service.FeedbackService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FeedbackController TEST
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
@SpringBootTest(classes = MockSrv.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class FeedbackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService service;

    @Test
    void injectedThenNotNull() {
        assertThat(mockMvc).isNotNull();
        assertThat(service).isNotNull();
    }

    @Test
    @WithMockUser
    void whenSaveNewFeedbackThenReturnStatusCreated() throws Exception {
        var feedbackDTO = new FeedbackDTO(1, 1, 1,
                2, "text", 5);
        when(service.save(feedbackDTO)).thenReturn(Optional.of(feedbackDTO));
        mockMvc.perform(post("/feedback/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(feedbackDTO)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void whenSaveNewFeedbackThenReturnStatusNotFound() throws Exception {
        var feedbackDTO = new FeedbackDTO(1, 1, 1,
                2, "text", 5);
        when(service.save(feedbackDTO)).thenReturn(Optional.empty());
        mockMvc.perform(post("/feedback/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(feedbackDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void whenFindAllByInterviewIdThenReturnStatusOkListDTO() throws Exception {
        var feedbackDTO = new FeedbackDTO(1, 1, 1,
                2, "text", 5);
        var feedbackDTO1 = new FeedbackDTO(2, 1, 1,
                1, "text1", 5);
        var listDTO = List.of(feedbackDTO, feedbackDTO1);
        when(service.findByInterviewId(feedbackDTO.getInterviewId())).thenReturn(listDTO);
        mockMvc.perform(get("/feedback/" + feedbackDTO.getInterviewId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(listDTO.size())))
                .andExpect(jsonPath("$[0].id", Matchers.is(feedbackDTO.getId())));
    }

    @Test
    @WithMockUser
    void whenFindAllByInterviewIdThenReturnStatusOkEmptyList() throws Exception {
        when(service.findByInterviewId(anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/feedback/" + anyInt()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    @WithMockUser
    void whenFindByInterviewIdAndUserIdThenReturnStatusOkFeedbackDTO() throws Exception {
        var feedbackDTO = new FeedbackDTO(1, 1, 1,
                2, "text", 5);
        when(service.findByInterviewIdAndUserId(feedbackDTO.getInterviewId(), feedbackDTO.getUserId())).thenReturn(List.of(feedbackDTO));
        mockMvc.perform(get("/feedback/")
                        .param("iId", String.valueOf(feedbackDTO.getInterviewId()))
                        .param("uId", String.valueOf(feedbackDTO.getUserId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(feedbackDTO.getId())))
                .andExpect(jsonPath("$[0].interviewId", Matchers.is(feedbackDTO.getInterviewId())))
                .andExpect(jsonPath("$[0].userId", Matchers.is(feedbackDTO.getUserId())));
    }

    @Test
    @WithMockUser
    void whenFindByInterviewIdAndUserIdThenReturnStatusNotFound() throws Exception {
        var feedbackDTO = new FeedbackDTO(1, 1, 1,
                2, "text", 5);
        when(service.findByInterviewIdAndUserId(feedbackDTO.getInterviewId(), feedbackDTO.getUserId())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/feedback/")
                        .param("iId", String.valueOf(feedbackDTO.getInterviewId()))
                        .param("uId", String.valueOf(feedbackDTO.getUserId())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}