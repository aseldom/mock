package ru.checkdev.mock.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Subscribe;
import ru.checkdev.mock.service.SubscribeService;

/**
 * SubscribeController rest controller для работы с подпиской
 *
 * @author Alexander
 * @since 07.03.2024
 */
@RestController
@RequestMapping("/subscribe")
@AllArgsConstructor
@Slf4j
public class SubscribeController {

    private final SubscribeService service;

    @PostMapping
    public ResponseEntity<Object> subscribe(@RequestBody long chatId) {
        Subscribe result = service.save(new Subscribe(0, chatId));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Integer> unsubscribe(@PathVariable long chatId) {
        int res = service.deleteByChatId(chatId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
