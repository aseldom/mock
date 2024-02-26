package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.site.dto.PersonDTO;

/**
 * CheckDev пробное собеседование
 * Сервис обработки пользователя (получение отправка)
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 18.09.2023
 */
@Service
@Slf4j
@AllArgsConstructor
public class PersonService {
    private static final String URL_PERSON_CURRENT = "/person/current";
    private static final String URL_PERSON_UPDATE = "/person/updateMultipart";
    private final WebClientAuthCall webClientAuthCall;

    /**
     * Метод получает модель PersonDTO по токену.
     *
     * @param token String
     * @return PersonDTO
     * @throws JsonProcessingException Exception
     */
    public PersonDTO getPerson(String token) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        String web = webClientAuthCall
                .doGet(URL_PERSON_CURRENT, token).block();
        return objectMapper.readValue(web, PersonDTO.class);
    }

    /**
     * Метод сохраняет изменения пользователя и фото пользователя.
     *
     * @param token     String
     * @param personDTO PersonDTO
     * @param file      MultipartFile
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> postUpdatePerson(String token, PersonDTO personDTO, MultipartFile file) {
        var builder = new MultipartBodyBuilder();
        builder.part("person", personDTO, MediaType.APPLICATION_JSON);
        builder.part("file", file.getResource(), MediaType.IMAGE_JPEG);
        return webClientAuthCall.doPostMultipart(URL_PERSON_UPDATE, token, builder)
                .block();
    }
}
