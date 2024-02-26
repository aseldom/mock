package ru.job4j.site.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.dto.TopicDTO;
import ru.job4j.site.service.TopicsService;

import java.util.List;

@RestController
@RequestMapping("/topics_rest")
@AllArgsConstructor
public class TopicsRestController {

    private final TopicsService topicsService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<TopicDTO>> getTopics(@PathVariable int categoryId)
            throws JsonProcessingException {
        return new ResponseEntity<>(topicsService.getByCategory(categoryId), HttpStatus.OK);
    }
}