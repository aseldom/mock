package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;
import ru.checkdev.mock.mapper.WisherMapper;
import ru.checkdev.mock.service.InterviewService;
import ru.checkdev.mock.service.WisherService;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/wisher")
@AllArgsConstructor
public class WisherController {

    private final InterviewService interviewService;
    private final WisherService wisherService;

    @PostMapping("/")
    public ResponseEntity<Wisher> save(@Valid @RequestBody WisherDto wisherDto) throws SQLException {
        Optional<Interview> interviewOptional = interviewService.findById(wisherDto.getInterviewId());
        if (interviewOptional.isEmpty()) {
            throw new SQLException("This interview is missing");
        }
        Optional<Wisher> rsl = wisherService.save(
                new WisherMapper().getWisher(wisherDto, interviewOptional.get()));
        if (rsl.isEmpty()) {
            throw new SQLException("An error occurred while saving data");
        }
        return  rsl
                .map(wisher -> new ResponseEntity<>(wisher, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Wisher> getById(@Valid @PathVariable int id) throws SQLException {
        Optional<Wisher> rsl = wisherService.findById(id);
        if (rsl.isEmpty()) {
            throw new SQLException("There is no wisher with this number");
        }
        return  rsl
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @PutMapping("/")
    public ResponseEntity<Wisher> update(@Valid @RequestBody WisherDto wisherDto) throws SQLException {
        Optional<Wisher> optionalWisher = wisherService.findById(wisherDto.getId());
        if (optionalWisher.isEmpty()) {
            throw new SQLException("There is no wisher with this number");
        }
        Wisher rsl = optionalWisher.get();
        Optional<Interview> optionalInterview = interviewService.findById(wisherDto.getInterviewId());
        if (optionalInterview.isEmpty()) {
            throw new SQLException("There is no interview with this number");
        }
        rsl.setInterview(optionalInterview.get());
        rsl.setUserId(wisherDto.getUserId());
        rsl.setContactBy(wisherDto.getContactBy());
        rsl.setApprove(wisherDto.isApprove());
        return new ResponseEntity<Wisher>(rsl,
                wisherService.update(rsl) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Wisher> delete(@Valid @PathVariable int id) {
        Wisher wisher = new Wisher();
        wisher.setId(id);
        return new ResponseEntity<Wisher>(wisher,
                wisherService.delete(wisher) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }
}
