package ru.job4j.site.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.site.service.PhotoServices;

/**
 * CheckDev пробное собеседование
 * PhotoController rest controller для получения Auth по ID.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 28.09.2023
 */
@RestController
@RequestMapping("/photo")
@AllArgsConstructor
@Slf4j
public class PhotoController {
    private final PhotoServices photoServices;

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getPhoto(@PathVariable int id) {
        return photoServices.getPhotoById(id);
    }
}
