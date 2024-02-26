package ru.checkdev.desc.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.desc.domain.Category;
import ru.checkdev.desc.service.CategoryService;

import java.util.List;

@RequestMapping("/categories")
@RestController
@AllArgsConstructor
public class CategoriesControl {
    private final CategoryService categoryService;

    @GetMapping("/")
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/most_pop")
    public List<Category> getMostPopular() {
        return categoryService.getMostPopular();
    }
}
