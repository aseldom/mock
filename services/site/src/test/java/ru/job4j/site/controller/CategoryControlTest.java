package ru.job4j.site.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.site.SiteSrv;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.CategoryDTO;
import ru.job4j.site.dto.UserInfoDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.CategoriesService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CategoryControl
 * Test
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 09.10.2023
 */
@SpringBootTest(classes = SiteSrv.class)
@AutoConfigureMockMvc
class CategoryControlTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoriesService categoriesService;
    @MockBean
    private AuthService authService;

    @Test
    void whenGetCreateFormThenReturnViewCrateForm() throws Exception {
        var userInfo = new UserInfoDTO();
        userInfo.setUsername("username");
        userInfo.setEmail("email");
        var token = "1234";
        var breadcrumbs = List.of(
                new Breadcrumb("Главная", "/index"),
                new Breadcrumb("Категории", "/categories/"),
                new Breadcrumb("Создать категорию", "/category/createForm"));
        when(authService.userInfo(token)).thenReturn(userInfo);
        mockMvc.perform(get("/category/createForm")
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(model().attribute("userInfo", userInfo))
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(status().isOk())
                .andExpect(view().name("categories/createForm"));
    }

    @Test
    void whenPostCreateCategoryThenReturnRedirectView() throws Exception {
        var token = "1234";
        var categoryDTO = new CategoryDTO(0, "name", 1, 1, 1);
        when(categoriesService.create(token, categoryDTO)).thenReturn(categoryDTO);
        mockMvc.perform(post("/category/")
                        .requestAttr("category", categoryDTO)
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/"));
    }

    @Test
    void whenGetEditFormWhenReturnViewEditCategoryForm() throws Exception {
        var userInfo = new UserInfoDTO();
        userInfo.setUsername("username");
        userInfo.setEmail("email");
        var token = "1234";
        var id = 1;
        var name = "categoryName";
        var breadcrumbs = List.of(
                new Breadcrumb("Главная", "/index"),
                new Breadcrumb("Категории", "/categories/"),
                new Breadcrumb("Редактировать категорию",
                        String.format("/category/editForm/%d/%s", id, name)));
        when(authService.userInfo(token)).thenReturn(userInfo);
        mockMvc.perform(get("/category/editForm/{id}/{name}", id, name)
                        .sessionAttr("token", token))
                .andDo(print())
                .andExpect(model().attribute("category", new CategoryDTO(id, name)))
                .andExpect(model().attribute("userInfo", userInfo))
                .andExpect(model().attribute("breadcrumbs", breadcrumbs))
                .andExpect(status().isOk())
                .andExpect(view().name("categories/editCategoryForm"));
    }

    @Test
    void whenPostUpdateCategoryThenReturnRedirectCategories() throws Exception {
        var token = "1234";
        var categoryDTO = new CategoryDTO(1, "name", 1, 1, 1);
        mockMvc.perform(post("/category/update")
                        .sessionAttr("token", token)
                        .requestAttr("category", categoryDTO))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories/"));
    }
}