package ru.checkdev.notification.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.service.SubscribeCategoryService;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
public class SubscribeCategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscribeCategoryService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    private final SubscribeCategory subscribeCategory = new SubscribeCategory(1, 2, 2);

    @Test
    @WithMockUser
    public void whenFindCategoriesByUserId() throws Exception {
        when(service.findCategoriesByUserId(subscribeCategory.getUserId())).thenReturn(List.of(subscribeCategory.getUserId()));
        mockMvc.perform(get("/subscribeCategory/2"))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string("[2]"));
    }
}