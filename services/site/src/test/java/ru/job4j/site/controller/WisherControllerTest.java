package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.domain.StatusWisher;
import ru.job4j.site.dto.WisherDto;
import ru.job4j.site.service.InterviewService;
import ru.job4j.site.service.WisherServiceWebClient;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.10.2023
 */
@SpringBootTest(classes = WisherController.class)
@AutoConfigureMockMvc
class WisherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WisherServiceWebClient wisherService;
    @MockBean
    private InterviewService interviewService;

    @Test
    void whenCreateWisherThenReturnRedirect() throws Exception {
        var wisher = new WisherDto(1, 2, 3, "mail", true, StatusWisher.IS_CONSIDERED.getId());
        var token = "1234";
        when(wisherService.saveWisherDto(token, wisher)).thenReturn(true);
        this.mockMvc.perform(post("/wisher/create")
                        .sessionAttr("token", token)
                        .param("interviewId", String.valueOf(wisher.getInterviewId()))
                        .param("userId", String.valueOf(wisher.getUserId()))
                        .param("contactBy", wisher.getContactBy()))
                .andDo(print())
                .andExpect(view().name("redirect:/interview/" + wisher.getInterviewId()));
    }

    @Test
    void whenDismissedWisherThenRedirectInterviewDetailPage() throws Exception {
        var token = "1234";
        var interviewId = 1;
        var wisherId = 2;
        var newStatusId = 22;
        doNothing().when(interviewService).updateStatus(token, interviewId, newStatusId);
        this.mockMvc.perform(post("/wisher/dismissed")
                        .sessionAttr("token", token)
                        .param("interviewId", String.valueOf(interviewId))
                        .param("wisherId", String.valueOf(wisherId)))
                .andDo(print())
                .andExpect(view().name("redirect:/interview/" + interviewId));
    }
}