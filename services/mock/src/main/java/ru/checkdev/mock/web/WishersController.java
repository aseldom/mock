package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;
import ru.checkdev.mock.service.InterviewService;
import ru.checkdev.mock.service.WisherService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/wishers")
@AllArgsConstructor
public class WishersController {

    private final WisherService wisherService;

    private final InterviewService interviewService;

    @GetMapping("/")
    public ResponseEntity<List<Wisher>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Wisher>> findByInterview(@Valid @PathVariable int id) throws SQLException {
        Optional<Interview> interviewOptional = interviewService.findById(id);
        if (interviewOptional.isEmpty()) {
            throw new SQLException("This interview is missing");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findByInterview(interviewOptional.get()));
    }

    @GetMapping("/dto/")
    public ResponseEntity<List<WisherDto>> findAllWisherDto() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findAllWisherDto());
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<List<WisherDto>> findDtoByInterview(@Valid @PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wisherService.findWisherDtoByInterviewId(id));
    }

    /**
     * Метод устанавливает новый статус участников собеседования.
     * В методе предается:
     * ID interview, статус всех участников данного интервью подлежит изменению.
     * ID wisher, данному участнику будет установлен статус параметра newStatus.
     * Всем остальным участникам данного собеседования будет установлен статус параметра anyStatus.
     *
     * @param param Map<String, String>
     * @return ResponseEntity
     */
    @PostMapping("/status/")
    public ResponseEntity<HttpStatus> setWisherStatus(@RequestParam Map<String, String> param) {
        var interviewId = Integer.parseInt(param.get("interviewId"));
        var wisherId = Integer.parseInt(param.get("wisherId"));
        var newStatusId = Integer.parseInt(param.get("newStatusId"));
        var anyStatusId = Integer.parseInt(param.get("anyStatusId"));
        wisherService.setWisherStatus(interviewId, wisherId, newStatusId, anyStatusId);
        return ResponseEntity.ok().build();
    }
}
