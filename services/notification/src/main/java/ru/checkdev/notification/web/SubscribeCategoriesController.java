package ru.checkdev.notification.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.service.SubscribeCategoryService;

import java.util.List;

@RestController
@RequestMapping("/subscribeCategory")
@AllArgsConstructor
public class SubscribeCategoriesController {
    private final SubscribeCategoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<List<Integer>> findCategoriesByUserId(@PathVariable int id) {
        List<Integer> list = service.findCategoriesByUserId(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<SubscribeCategory> toAddSubscribeCategory(
            @RequestBody SubscribeCategory subscribeCategory
    ) {
        var created = service.save(subscribeCategory);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<SubscribeCategory> toDeleteSubscribeCategory(
            @RequestBody SubscribeCategory subscribeCategory
    ) {
        var deleted = service.delete(subscribeCategory);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}