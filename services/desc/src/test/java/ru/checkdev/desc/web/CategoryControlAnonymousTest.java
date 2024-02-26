package ru.checkdev.desc.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.desc.DescSrv;
import ru.checkdev.desc.domain.Category;
import ru.checkdev.desc.service.CategoryService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DescSrv.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class CategoryControlAnonymousTest {
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenPingWithoutAuth() throws Exception {
        mockMvc.perform(get("/category/ping"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType("text/plain;charset=UTF-8"),
                        content().string("{pong}")
                );
    }

    @Test
    void whenCheckWithoutAuth() throws Exception {
        mockMvc.perform(get("/category/check"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType("text/plain;charset=UTF-8"),
                        content().string("check")
                );
    }

    @Test
    void whenGetWithoutAuth() throws Exception {
        var category = new Category();
        category.setId(1);
        category.setName("test");
        category.setTotal(10);
        category.setPosition(33);
        when(categoryService.findById(1)).thenReturn(Optional.of(category));
        mockMvc.perform(get("/category/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string("{\"id\":1,\"name\":\"test\",\"total\":10,\"position\":33}")
                );
    }

    @Test
    void whenGetWithoutAuthThenReturnEmpty() throws Exception {
        mockMvc.perform(get("/category/1"))
                .andDo(print())
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    void whenTryCreateWithoutAuthThenReturn4xx() throws Exception {
        mockMvc.perform(post("/category/"))
                .andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }
}