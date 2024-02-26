package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.service.InterviewService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/interviews")
@AllArgsConstructor
public class InterviewsController {

    private final InterviewService interviewService;

    /*Аннотация не работает
    @PreAuthorize("isAuthenticated()") */
    @GetMapping("/")
    public ResponseEntity<Page<Interview>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    ) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findPaging(page, size));
    }

    @GetMapping("/{mode}")
    public ResponseEntity<List<Interview>> findByMode(@PathVariable int mode) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findByMode(mode));
    }

    @GetMapping("/findByTopicId/{topicId}")
    public ResponseEntity<Page<Interview>> findByTopicId(
            @PathVariable int topicId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(interviewService.findByTopicId(topicId, page, size));
    }

    @GetMapping("/findByTopicsIds/{tids}")
    public ResponseEntity<Page<Interview>> findByCategory(
            @PathVariable String tids,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        var topicIdsArr = tids.split(",");
        List<Integer> topicIds = new ArrayList<>();
        for (String id : topicIdsArr) {
            topicIds.add(Integer.valueOf(id));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(interviewService.findByTopicsIds(topicIds, page, size));
    }
}
