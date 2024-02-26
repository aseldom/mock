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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DescSrv.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CategoriesControlAnonymousTest {
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetAllWithoutAuth() throws Exception {
        var category = new Category();
        category.setId(1);
        when(categoryService.getAll()).thenReturn(List.of(category));
        mockMvc.perform(get("/categories/"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$..id").value(1)
                );
    }
}
