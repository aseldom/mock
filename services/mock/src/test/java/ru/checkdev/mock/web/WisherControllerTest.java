package ru.checkdev.mock.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
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
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.service.InterviewService;
import ru.checkdev.mock.service.WisherService;

import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
@AutoConfigureMockMvc
class WisherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WisherService wisherService;

    @MockBean
    private InterviewService interviewService;

    private Interview interview = Interview.of()
            .id(1)
            .mode(2)
            .submitterId(3)
            .title("test_title")
            .additional("test_additional")
            .contactBy("test_contact_by")
            .approximateDate("test_approximate_date")
            .createDate(null)
            .build();

    private Wisher wisher = Wisher.of()
            .id(1)
            .interview(interview)
            .userId(1)
            .contactBy("test_contact_by")
            .approve(true)
            .build();

    private String interviewString = new GsonBuilder().serializeNulls().create().toJson(interview);

    private String wisherString = new GsonBuilder().serializeNulls().create().toJson(wisher);

    private Wisher emptyWisher = Wisher.of()
            .id(1)
            .interview(null)
            .userId(0)
            .contactBy(null)
            .approve(false)
            .build();

    private String emptyWisherString = new GsonBuilder().serializeNulls().create().toJson(emptyWisher);

    @Test
    @WithMockUser
    public void whenSaveAndGetTheSame() throws Exception {
        when(interviewService.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        when(wisherService.save(any(Wisher.class))).thenReturn(Optional.of(wisher));
        mockMvc.perform(post("/wisher/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wisher)))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(wisherString));
    }

    @Test
    @WithMockUser
    public void whenGetByIdIsCorrect() throws Exception {
        when(wisherService.findById(any(Integer.class))).thenReturn(Optional.of(wisher));
        this.mockMvc.perform(get("/wisher/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(wisherString));
    }

    @Test
    @WithMockUser
    public void whenGetByIdIsEmpty() throws Exception {
        when(wisherService.findById(any(Integer.class))).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/wisher/1"))
                .andDo(print())
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenTryToUpdateIsCorrect() throws Exception {
        when(interviewService.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        when(wisherService.findById(any(Integer.class))).thenReturn(Optional.of(wisher));
        when(wisherService.update(any(Wisher.class))).thenReturn(true);
        this.mockMvc.perform(put("/wisher/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wisher)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(wisherString));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenTryToUpdateIsNotCorrect() throws Exception {
        when(interviewService.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        when(wisherService.findById(any(Integer.class))).thenReturn(Optional.of(wisher));
        when(wisherService.update(any(Wisher.class))).thenReturn(false);
        this.mockMvc.perform(put("/wisher/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wisher)))
                .andDo(print())
                .andExpectAll(
                        status().isNoContent(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(wisherString));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenDeleteIsCorrect() throws Exception {
        when(wisherService.delete(any(Wisher.class))).thenReturn(true);
        this.mockMvc.perform(delete("/wisher/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(emptyWisherString));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenDeleteIsNotCorrect() throws Exception {
        when(wisherService.delete(any(Wisher.class))).thenReturn(false);
        this.mockMvc.perform(delete("/wisher/1"))
                .andDo(print())
                .andExpectAll(
                        status().isNoContent(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(emptyWisherString));
    }
}