package ru.checkdev.desc.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.desc.domain.Category;
import ru.checkdev.desc.service.CategoryService;

@RequestMapping("/category")
@RestController
@AllArgsConstructor
public class CategoryControl {
    private final CategoryService categoryService;

    @GetMapping("/ping")
    public String ping() {
        return "{pong}";
    }

    @GetMapping("/check")
    public String check() {
        return "check";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable int id) {
        var person = categoryService.findById(id);
        if (person.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> create(@RequestBody Category category) {
        var created = categoryService.create(category);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Category category) {
        categoryService.update(category);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable int categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/statistic")
    public ResponseEntity<Void> updateStatistic(@RequestBody int categoryId) {
        categoryService.updateStatistic(categoryId);
        return ResponseEntity.ok().build();
    }
}
