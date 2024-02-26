package ru.job4j.site.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ImageCompressor interface
 * описывает поведения классов обработки изображения.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 04.10.2023
 */
public interface ImageCompress {
    MultipartFile compressImage(MultipartFile file) throws IOException;
}
