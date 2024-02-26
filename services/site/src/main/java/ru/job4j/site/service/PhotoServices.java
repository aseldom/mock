package ru.job4j.site.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * CheckDev пробное собеседование
 * PhotoService загрузки изображения из сервиса Auth
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 27.09.2023
 */
@Service
@AllArgsConstructor
@Slf4j
public class PhotoServices {
    private static final String URL_IMG = "/img";
    private final WebClientAuthCall webClientAuthCall;

    /**
     * Загрузка изображения по ID
     *
     * @param id IMG ID
     * @return ByteArrayResource
     */
    public ResponseEntity<ByteArrayResource> getPhotoById(int id) {
        return webClientAuthCall
                .doGetPhoto(URL_IMG, id)
                .block();
    }
}
