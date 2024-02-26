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
import ru.checkdev.mock.service.InterviewService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
@AutoConfigureMockMvc
class InterviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewService service;

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

    private String string = new GsonBuilder().serializeNulls().create().toJson(interview);

    private Interview emptyInterview = Interview.of()
            .id(1)
            .mode(0)
            .submitterId(0)
            .title(null)
            .additional(null)
            .contactBy(null)
            .approximateDate(null)
            .createDate(null)
            .build();

    private String emptyString = new GsonBuilder().serializeNulls().create().toJson(emptyInterview);

    @Test
    @WithMockUser
    public void whenSaveAndGetTheSame() throws Exception {
        when(service.save(any(Interview.class))).thenReturn(Optional.of(interview));
        mockMvc.perform(post("/interview/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interview)))
                .andDo(print())
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(string));
    }

    @Test
    @WithMockUser
    public void whenGetByIdIsCorrect() throws Exception {
        when(service.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        this.mockMvc.perform(get("/interview/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(string));
    }

    @Test
    @WithMockUser
    public void whenGetByIdIsEmpty() throws Exception {
        when(service.findById(any(Integer.class))).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/interview/1"))
                .andDo(print())
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenTryToUpdateIsCorrect() throws Exception {
        when(service.update(any(Interview.class))).thenReturn(true);
        this.mockMvc.perform(put("/interview/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interview)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(string));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenTryToUpdateIsNotCorrect() throws Exception {
        when(service.update(any(Interview.class))).thenReturn(false);
        this.mockMvc.perform(put("/interview/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interview)))
                .andDo(print())
                .andExpectAll(
                        status().isNoContent(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(string));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenDeleteIsCorrect() throws Exception {
        when(service.delete(any(Interview.class))).thenReturn(true);
        this.mockMvc.perform(delete("/interview/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(emptyString));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void whenDeleteIsNotCorrect() throws Exception {
        when(service.delete(any(Interview.class))).thenReturn(false);
        this.mockMvc.perform(delete("/interview/1"))
                .andDo(print())
                .andExpectAll(
                        status().isNoContent(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(emptyString));
    }

    @Test
    @WithMockUser
    public void whenUpdateStatusThenReturnStatusOk() throws Exception {
        when(service.updateStatus(anyInt(), anyInt())).thenReturn(true);
        this.mockMvc.perform(put("/interview/status/")
                        .param("id", "1")
                        .param("newStatus", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateStatusThenReturnStatusNotFound() throws Exception {
        when(service.updateStatus(anyInt(), anyInt())).thenReturn(false);
        this.mockMvc.perform(put("/interview/status/")
                        .param("id", "1")
                        .param("newStatus", "2"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}