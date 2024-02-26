package ru.checkdev.mock.web;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.repository.InterviewRepository;
import ru.checkdev.mock.service.InterviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
@AutoConfigureMockMvc
class InterviewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewService service;

    @MockBean
    private InterviewRepository interviewRepository;

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

    @Test
    @WithMockUser
    public void whenGetAll() throws Exception {
        var page = new PageImpl<>(List.of(interview));
        when(interviewRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(page);
        when(service.findPaging(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(page);
        mockMvc.perform(get("/interviews/"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    public void whenFindByTopicsIds() throws Exception {
        List<Interview> interviews = new ArrayList<>();
        IntStream.range(1, 8).forEach(i -> {
            var interview = Interview.of()
                    .id(i)
                    .mode(2)
                    .submitterId(3)
                    .title(String.format("interview_%d", i))
                    .additional("test_additional")
                    .contactBy("test_contact_by")
                    .approximateDate("test_approximate_date")
                    .createDate(null)
                    .build();
            interviews.add(interview);
        });
        var page = new PageImpl<>(interviews);
        when(interviewRepository.findByTopicIdIn(List.of(1, 2, 3), PageRequest.of(0, 5)))
                .thenReturn(page);
        when(service.findPaging(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(page);
        mockMvc.perform(get("/interviews/findByTopicsIds/1,2,3"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON));
    }
}