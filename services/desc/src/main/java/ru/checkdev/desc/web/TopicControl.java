package ru.checkdev.desc.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.desc.domain.Topic;
import ru.checkdev.desc.service.TopicService;

@RequestMapping("/topic")
@RestController
@AllArgsConstructor
public class TopicControl {
    private final TopicService topicService;

    @GetMapping("/{id}")
    public ResponseEntity<Topic> findById(@PathVariable int id) {
        var topic = topicService.findById(id);
        topicService.incrementTotal(id);
        return topic.map(
                value -> new ResponseEntity<>(value, HttpStatus.OK)
        ).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getNameById(@PathVariable int id) {
        var name = topicService.getNameById(id);
        return name.map(
                value -> new ResponseEntity<>(value, HttpStatus.OK)
        ).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Topic> create(@RequestBody Topic topic) {
        var created = topicService.create(topic);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> update(@RequestBody Topic topic) {
        topicService.update(topic);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@RequestBody Topic topic) {
        topicService.delete(topic.getId());
        return ResponseEntity.ok().build();
    }
}
