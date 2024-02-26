package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.dto.FeedbackDTO;
import ru.checkdev.mock.service.FeedbackService;

import java.util.List;

/**
 * FeedbackController rest controller для работы с сущностью Feedback
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
@RestController
@RequestMapping("/feedback")
@AllArgsConstructor
@Slf4j
public class FeedbackController {
    private final FeedbackService service;

    @PostMapping("/")
    public ResponseEntity<FeedbackDTO> saveNewFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        var result = service.save(feedbackDTO);
        return new ResponseEntity<>(
                result.orElse(new FeedbackDTO()),
                result.isPresent() ? HttpStatus.CREATED : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<FeedbackDTO>> findAllByInterviewId(@PathVariable("id") int interviewId) {
        var result = service.findByInterviewId(interviewId);
        return ResponseEntity.ok(result);
    }

    /**
     * Метод возвращает отзыв по ID Interview и ID User
     *
     * @param interviewId int ID Interview
     * @param userId      int ID User
     * @return ResponseEntity
     */
    @GetMapping("/")
    public ResponseEntity<List<FeedbackDTO>> findByInterviewIdUserId(@RequestParam("iId") int interviewId,
                                                                     @RequestParam("uId") int userId) {
        var result = service.findByInterviewIdAndUserId(interviewId, userId);
        return new ResponseEntity<>(
                result,
                result.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
