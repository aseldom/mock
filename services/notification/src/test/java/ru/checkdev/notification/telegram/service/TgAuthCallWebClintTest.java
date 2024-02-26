package ru.checkdev.notification.telegram.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;

import java.util.Calendar;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Testing TgAuthCallWebClint
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 06.10.2023
 */
@ExtendWith(MockitoExtension.class)
class TgAuthCallWebClintTest {
    private static final String URL = "http://tetsurl:15000";
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private TgAuthCallWebClint tgAuthCallWebClint;

    @BeforeEach
    void setUp() {
        tgAuthCallWebClint = new TgAuthCallWebClint(URL);
        tgAuthCallWebClint.setWebClient(webClientMock);
    }


    @Test
    void whenDoGetThenReturnPersonDTO() {
        Integer personId = 100;
        var created = new Calendar.Builder()
                .set(Calendar.DAY_OF_MONTH, 23)
                .set(Calendar.MONTH, Calendar.OCTOBER)
                .set(Calendar.YEAR, 2023)
                .build();
        var personDto = new PersonDTO("mail", "password", true, Collections.EMPTY_LIST, created);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("/person/" + personId)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(PersonDTO.class)).thenReturn(Mono.just(personDto));
        PersonDTO actual = tgAuthCallWebClint.doGet("/person/" + personId).block();
        assertThat(actual).isEqualTo(personDto);
    }

    @Test
    void whenDoGetThenReturnExceptionError() {
        Integer personId = 100;
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("/person/" + personId)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(PersonDTO.class)).thenReturn(Mono.error(new Throwable("Error")));
        assertThatThrownBy(() -> tgAuthCallWebClint.doGet("/person/" + personId).block())
                .isInstanceOf(Throwable.class)
                .hasMessageContaining("Error");
    }

    @Test
    void whenDoPostSavePersonThenReturnNewPerson() {
        var created = new Calendar.Builder()
                .set(Calendar.DAY_OF_MONTH, 23)
                .set(Calendar.MONTH, Calendar.OCTOBER)
                .set(Calendar.YEAR, 2023)
                .build();
        var personDto = new PersonDTO("mail", "password", true, null, created);
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri("/person/created")).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(personDto)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(Object.class)).thenReturn(Mono.just(personDto));
        Mono<Object> objectMono = tgAuthCallWebClint.doPost("/person/created", personDto);
        PersonDTO actual = (PersonDTO) objectMono.block();
        assertThat(actual).isEqualTo(personDto);
    }
}