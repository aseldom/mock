package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.dto.CategoryDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.CategoriesService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryControl {
    private final CategoriesService categoriesService;
    private final AuthService authService;

    @GetMapping("/createForm")
    public String createForm(Model model, HttpServletRequest req) throws JsonProcessingException {
        var userInfo = authService.userInfo(getToken(req));
        model.addAttribute("userInfo", userInfo);
        RequestResponseTools.addAttrCanManage(model, userInfo);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Категории", "/categories/",
                "Создать категорию", "/category/createForm"
        );
        return "categories/createForm";
    }

    @PostMapping("/")
    public String createCategory(@ModelAttribute CategoryDTO category, HttpServletRequest req)
            throws JsonProcessingException {
        categoriesService.create(getToken(req), category);
        return "redirect:/categories/";
    }

    @GetMapping("/editForm/{id}/{name}")
    public String editForm(@PathVariable("id") int id, @PathVariable("name") String name,
                           Model model, HttpServletRequest req) throws JsonProcessingException {
        model.addAttribute("category", new CategoryDTO(id, name));
        var token = getToken(req);
        if (token != null) {
            var userInfo = authService.userInfo(token);
            RequestResponseTools.addAttrCanManage(model, userInfo);
        }
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Категории", "/categories/",
                "Редактировать категорию", String.format("/category/editForm/%d/%s", id, name)
        );
        return "categories/editCategoryForm";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute CategoryDTO category,
                                 HttpServletRequest req) throws JsonProcessingException {
        var token = getToken(req);
        categoriesService.update(token, category);
        return "redirect:/categories/";
    }
}
