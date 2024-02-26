package ru.job4j.site.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 04.10.2023
 */
class ImageCompressorServiceTest {
    private final Path testFile = Path.of("src", "test", "resources", "testfiles", "olafAva.jpg");


    @Test
    void whenCompressImageThenReturnNotNullResource() throws IOException {
        byte[] bytes = Files.readAllBytes(testFile);
        MultipartFile multiFile = new MockMultipartFile(testFile.getFileName().toString(),
                bytes);
        var compressor = new ImageCompressorService("100");
        MultipartFile resource = compressor.compressImage(multiFile);
        assertThat(resource).isNotNull();
    }

    @Test
    void whenCompressorImageThenExpectSizeTrue() throws IOException {
        byte[] bytes = Files.readAllBytes(testFile);
        MultipartFile multiFile = new MockMultipartFile(testFile.getFileName().toString(), bytes);
        var compressor = new ImageCompressorService("500");
        BufferedImage originImage = ImageIO.read(testFile.toFile());
        int[] expectSize = compressor.getResizeImage(originImage.getWidth(), originImage.getHeight());
        MultipartFile resultFile = compressor.compressImage(multiFile);
        BufferedImage resultImage = ImageIO.read(resultFile.getInputStream());
        int[] actualSize = new int[]{resultImage.getWidth(), resultImage.getHeight()};
        assertThat(actualSize).isEqualTo(expectSize);
    }

    @Test
    void whenCompressorImageFileNullThenException() {
        MultipartFile multiFile = new MockMultipartFile(
                testFile.getFileName().toString(),
                testFile.getFileName().toString(),
                "image/jpeg",
                new byte[]{0});
        var compressor = new ImageCompressorService("500");
        assertThatThrownBy(() -> compressor.compressImage(multiFile))
                .isInstanceOf(Exception.class);
    }

    @Test
    void whenGetResizeImageW1500H2000ThenReturnW75H100() {
        var compressor = new ImageCompressorService("100");
        var originWidth = 1500;
        var originHeight = 2000;
        var expected = new int[]{75, 100};
        var actual = compressor.getResizeImage(originWidth, originHeight);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenGetResizeImageW2000H1500ThenReturnW100H75() {
        var compressor = new ImageCompressorService("100");
        var originWidth = 2000;
        var originHeight = 1500;
        var expected = new int[]{100, 75};
        var actual = compressor.getResizeImage(originWidth, originHeight);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenGetResizeImageW350H255ThenReturnW100H72() {
        var compressor = new ImageCompressorService("100");
        var originWidth = 350;
        var originHeight = 255;
        var expected = new int[]{100, 72};
        var actual = compressor.getResizeImage(originWidth, originHeight);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenGetResizeImageW100H100ThenReturnW100H100() {
        var compressor = new ImageCompressorService("100");
        var originWidth = 100;
        var originHeight = 100;
        var expected = new int[]{100, 100};
        var actual = compressor.getResizeImage(originWidth, originHeight);
        assertThat(actual).isEqualTo(expected);
    }
}