package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.*;
import ru.job4j.site.service.*;

import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
public class InterviewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewsService interviewsService;
    @MockBean
    private AuthService authService;
    @MockBean
    private ProfilesService profilesService;
    @MockBean
    private CategoriesService categoriesService;
    @MockBean
    private WisherService wisherService;
    @MockBean
    private FilterService filterService;
    @MockBean
    private TopicsService topicsService;

    @Test
    public void whenShowAllInterviews() throws Exception {
        var token = "1410";
        var id = 1;
        var profile = new ProfileDTO(id, "username", "experience", 1,
                Calendar.getInstance(), Calendar.getInstance());
        var userInfo = new UserInfoDTO();
        userInfo.setId(1);
        var breadcrumbs = List.of(
                new Breadcrumb("Главная", "/index"),
                new Breadcrumb("Собеседования", "/interviews/"));
        List<InterviewDTO> interviews = IntStream.range(0, 3).mapToObj(i -> {
            var interview = new InterviewDTO();
            interview.setId(i);
            interview.setMode(1);
            interview.setStatus(1);
            interview.setSubmitterId(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional("Some text");
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate("06.10.2023");
            return interview;
        }).toList();
        List<CategoryDTO> categories = IntStream.range(0, 3).mapToObj(i -> {
            var category = new CategoryDTO();
            category.setId(i);
            category.setName(String.format("category_%d", i));
            category.setPosition(1);
            category.setTotal(100);
            category.setTopicsSize(14);
            return category;
        }).toList();
        List<TopicIdNameDTO> topicIdNameDTOS = IntStream.range(1, 8).mapToObj(
                i -> new TopicIdNameDTO(i, String.format("topic_id_name_%d", i))
        ).toList();
        var filter = new FilterDTO(1, 1, 1);
        var page = new PageImpl<>(interviews);
        when(wisherService.getAllWisherDtoByInterviewId(token, "")).thenReturn(new ArrayList<>());
        when(wisherService.getInterviewStatistic(new ArrayList<>())).thenReturn(new HashMap<>());
        when(interviewsService.getAll(token, 1, 5)).thenReturn(page);
        when(interviewsService.getByTopicId(filter.getTopicId(), 1, 5)).thenReturn(page);
        when(authService.userInfo(token)).thenReturn(userInfo);
        when(profilesService.getProfileById(id)).thenReturn(Optional.of(profile));
        when(categoriesService.getAll()).thenReturn(categories);
        when(filterService.getByUserId(token, userInfo.getId())).thenReturn(filter);
        when(categoriesService.getNameById(categories, 1)).thenReturn(categories.get(1).getName());
        when(topicsService.getNameById(filter.getTopicId())).thenReturn("SOME TOPIC NAME");
        when(topicsService.getTopicIdNameDtoByCategory(id)).thenReturn(topicIdNameDTOS);
        mockMvc.perform(get("/interviews/")
                        .sessionAttr("token", token)
                        .param("page", "1")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(model().attribute("statisticMap", new HashMap<>()))
                .andExpect(model().attribute("interviewsPage", page))
                .andExpect(model().attribute("statuses", StatusInterview.values()))
                .andExpect(model().attribute("current_page", "interviews"))
                .andExpect(model().attribute("userInfo", userInfo))
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(model().attribute("users", Set.of(profile)))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("categoryName", "category_1"))
                .andExpect(model().attribute("topicName", "SOME TOPIC NAME"))
                .andExpect(model().attribute("filter", filter))
                .andExpect(model().attribute("topics", topicIdNameDTOS))
                .andExpect(status().isOk())
                .andExpect(view().name("interview/interviews"));
    }
}