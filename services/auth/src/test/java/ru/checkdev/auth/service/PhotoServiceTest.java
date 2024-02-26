package ru.checkdev.auth.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.checkdev.auth.domain.Photo;
import ru.checkdev.auth.repository.PhotoRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PhotoServiceTest {
    @Mock
    private PhotoRepository photoRepository;
    @InjectMocks
    private PhotoService photoService;

    private static final Photo PHOTO_FOR_SAVE = new Photo(new byte[]{1, 2, 3}, "some photo");
    private static final Photo PHOTO_IN_DB = new Photo(1, new byte[]{1, 2, 3}, "some photo");

    @Test
    void whenSavePhotoThenReturnPhotoWithId() {
        when(photoRepository.save(PHOTO_FOR_SAVE)).thenReturn(PHOTO_IN_DB);
        assertEquals(PHOTO_IN_DB, photoService.save(PHOTO_FOR_SAVE));
    }

    @Test
    void whenFindPhotoById() {
        when(photoRepository.findById(1)).thenReturn(Optional.of(PHOTO_IN_DB));
        assertEquals(PHOTO_IN_DB, photoService.findImage(1));
    }

    @Test
    void whenNotFindPhotoById() {
        when(photoRepository.findById(10)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> photoService.findImage(10));
    }

}