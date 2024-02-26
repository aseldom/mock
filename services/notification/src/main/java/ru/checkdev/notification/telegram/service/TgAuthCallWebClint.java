package ru.checkdev.notification.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;

/**
 * 3. Мидл
 * Класс реализует методы get и post для отправки сообщений через WebClient
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
@Service
@Slf4j
public class TgAuthCallWebClint {
    private WebClient webClient;

    public TgAuthCallWebClint(@Value("${server.auth}") String urlAuth) {
        this.webClient = WebClient.create(urlAuth);
    }

    /**
     * Метод get
     *
     * @param url URL http
     * @return Mono<Person>
     */
    public Mono<PersonDTO> doGet(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(PersonDTO.class)
                .doOnError(err -> log.error("API not found: {}", err.getMessage()));
    }

    /**
     * Метод POST
     *
     * @param url       URL http
     * @param personDTO Body PersonDTO.class
     * @return Mono<Person>
     */
    public Mono<Object> doPost(String url, PersonDTO personDTO) {
        return webClient
                .post()
                .uri(url)
                .bodyValue(personDTO)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnError(err -> log.error("API not found: {}", err.getMessage()));
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
