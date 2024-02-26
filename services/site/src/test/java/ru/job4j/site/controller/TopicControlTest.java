package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.*;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.NotificationService;
import ru.job4j.site.service.TopicsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
public class TopicControlTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TopicsService topicsService;
    @MockBean
    private AuthService authService;

    @MockBean
    private NotificationService notifications;

    @Test
    public void whenShowDetails() throws Exception {
        var token = "1410";
        var topic = new TopicDTO();
        topic.setId(1);
        topic.setName("Some topic");
        topic.setText("Some text");
        topic.setCreated(Calendar.getInstance());
        topic.setUpdated(Calendar.getInstance());
        topic.setPosition(33);
        CategoryDTO category = new CategoryDTO();
        category.setId(1);
        category.setName("Some category");
        category.setPosition(55);
        topic.setCategory(category);
        var userInfo = new UserInfoDTO();
        userInfo.setId(1);
        userInfo.setEmail("email");
        userInfo.setUsername("name");
        var userTopicDto = new UserTopicDTO();
        userTopicDto.setId(1);
        userTopicDto.setSubscribeTopicIds(new ArrayList<>());
        when(authService.userInfo(token)).thenReturn(userInfo);
        when(topicsService.getById(1)).thenReturn(topic);
        when(notifications.findTopicByUserId(userInfo.getId())).thenReturn(userTopicDto);
        mockMvc.perform(get("/topic/1")
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("topic/details"))
                .andExpect(model().attribute("topic", topic))
                .andExpect(model().attribute("userTopicDTO", userTopicDto))
                .andExpect(model().attribute("breadcrumbs", List.of(
                        new Breadcrumb("Главная", "/index"),
                        new Breadcrumb("Категории", "/categories/"),
                        new Breadcrumb("Some category", "/topics/1"),
                        new Breadcrumb("Some topic", "/topic/1"))));
    }

    @Test
    public void whenOpenCreateForm() throws Exception {
        var breadcrumbs = List.of(
                new Breadcrumb("Главная", "/index"),
                new Breadcrumb("Категории", "/categories/"),
                new Breadcrumb("Темы", "/topics/1"),
                new Breadcrumb("Создание темы", "/topic/createForm/1"));
        var token = "1410";
        var userInfo = new UserInfoDTO();
        userInfo.setEmail("email");
        userInfo.setUsername("name");
        when(authService.userInfo(token)).thenReturn(userInfo);
        mockMvc.perform(get("/topic/createForm/1")
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(model().attribute("categoryId", 1))
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(status().isOk())
                .andExpect(view().name("topic/createForm"));
    }

    @Test
    public void whenTopicCreated() throws Exception {
        var topic = new TopicLiteDTO();
        topic.setName("Some topic");
        topic.setPosition(33);
        topic.setText("Some text");
        mockMvc.perform(post("/topic/create")
                        .requestAttr("topic", topic))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/"));
    }

    @Test
    public void whenOpenUpdateForm() throws Exception {
        var token = "1410";
        var userInfo = new UserInfoDTO();
        var role = new Role();
        role.setId(1);
        role.setValue("ROLE_USER");
        userInfo.setRoles(List.of(role));
        var topic = new TopicDTO();
        topic.setId(1);
        topic.setName("Some topic");
        topic.setText("Some text");
        topic.setCreated(Calendar.getInstance());
        topic.setUpdated(Calendar.getInstance());
        topic.setPosition(33);
        CategoryDTO category = new CategoryDTO();
        category.setId(1);
        category.setName("Some category");
        topic.setCategory(category);
        when(authService.userInfo(token)).thenReturn(userInfo);
        when(topicsService.getById(1)).thenReturn(topic);
        mockMvc.perform(get("/topic/updateForm/1").sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("topic/updateForm"))
                .andExpect(model().attribute("topic", topic))
                .andExpect(model().attribute("breadcrumbs", List.of(
                        new Breadcrumb("Главная", "/index"),
                        new Breadcrumb("Категории", "/categories/"),
                        new Breadcrumb("Some category", "/topics/1"),
                        new Breadcrumb("Редактировать тему", "/topic/updateForm/1"))));
    }

    @Test
    public void whenTopicUpdated() throws Exception {
        var topic = new TopicDTO();
        topic.setId(1);
        topic.setName("Some topic");
        topic.setPosition(33);
        topic.setText("Some text");
        topic.setCreated(Calendar.getInstance());
        topic.setUpdated(Calendar.getInstance());
        CategoryDTO category = new CategoryDTO();
        category.setId(1);
        category.setName("Some category");
        topic.setCategory(category);
        mockMvc.perform(post("/topic/update")
                        .requestAttr("topic", topic)
                .param("id", String.valueOf(topic.getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/topic/" + topic.getId()));
    }

    @Test
    public void whenTopicDeleted() throws Exception {
        mockMvc.perform(post("/topic/delete")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/"));
    }
}