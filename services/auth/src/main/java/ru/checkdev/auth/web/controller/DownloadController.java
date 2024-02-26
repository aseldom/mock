package ru.checkdev.auth.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.checkdev.auth.domain.Photo;
import ru.checkdev.auth.service.PhotoService;

import java.io.IOException;

/**
 * .
 *
 * @author Hincu Andrei (andreih1981@gmail.com) by 30.05.18;
 * @version $Id$
 * @since 0.1
 */
@Controller
public class DownloadController {
    final private PhotoService photoService;

    @Autowired
    public DownloadController(final PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(value = "/img", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> deliveryImage(@RequestParam(value = "id") String id) throws IOException {
        Photo photo = photoService.findImage(Integer.valueOf(id));
        if (photo == null) {
            return ResponseEntity.badRequest().body(new ByteArrayResource(new byte[0]));
        }
        ByteArrayResource resource = new ByteArrayResource(photo.getPhoto());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + photo.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(photo.getPhoto().length)
                .body(resource);
    }

}
