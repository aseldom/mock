package ru.job4j.site.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.service.InterviewsService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@RestController
@RequestMapping("/interviews_rest")
@AllArgsConstructor
public class InterviewsRestController {

    private final InterviewsService interviewsService;

    @GetMapping("/")
    public ResponseEntity<Page<InterviewDTO>> getAll(
            HttpServletRequest req,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size)
            throws JsonProcessingException {
        return new ResponseEntity<>(
                interviewsService.getAll(getToken(req), page, size), HttpStatus.OK
        );
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<Page<InterviewDTO>> getByFilter(
            @PathVariable int topicId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size)
            throws JsonProcessingException {
        return new ResponseEntity<>(
                interviewsService.getByTopicId(topicId, page, size), HttpStatus.OK
        );
    }
}
