package ru.job4j.site.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.job4j.site.domain.StatusWisher;
import ru.job4j.site.dto.InterviewStatistic;
import ru.job4j.site.dto.WisherDto;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * WisherServiceWebClient TEST
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.10.2023
 */
@ExtendWith(MockitoExtension.class)
class WisherServiceWebClientTest {

    private static final String URL = "http://testurl:1005000";
    private final String urlWisher = "/wisher/";
    private final String urlWishers = "/wishers/";

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

    private WisherServiceWebClient wisherService;

    @BeforeEach
    void setUp() {
        wisherService = new WisherServiceWebClient(URL);
        wisherService.setWebClientWisher(webClientMock);
    }

    @Test
    void whenWisherServiceNotNull() {
        assertThat(wisherService).isNotNull();
    }


    @Test
    void whenSaveWisherThenReturnTrue() {
        WisherDto wisherDto = new WisherDto(1, 1, 1, "mail@mail.ru", true, StatusWisher.IS_CONSIDERED.getId());
        String token = "12345";
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(urlWisher)).thenReturn(requestBodyMock);
        when(requestBodyMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(wisherDto)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(WisherDto.class)).thenReturn(Mono.just(new ResponseEntity<>(wisherDto, HttpStatus.CREATED)));
        var saveWisher = wisherService.saveWisherDto(token, wisherDto);
        assertThat(saveWisher).isTrue();
    }

    @Test
    void whenSaveWisherThenReturnFalse() {
        var wisherDto = new WisherDto();
        String token = "12345";
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(urlWisher)).thenReturn(requestBodyMock);
        when(requestBodyMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(wisherDto)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(WisherDto.class)).thenReturn(Mono.just(new ResponseEntity<>(wisherDto, HttpStatus.CONFLICT)));
        var saveWisher = wisherService.saveWisherDto(token, wisherDto);
        assertThat(saveWisher).isFalse();
    }


    @Test
    void whenGetAllWisherByInterviewIdThenListWisherDto() {
        var interviewId = "2";
        var wisherDto0 = new WisherDto(1, Integer.parseInt(interviewId), 3, "mail1@", true, StatusWisher.IS_CONSIDERED.getId());
        var wisherDto1 = new WisherDto(1, Integer.parseInt(interviewId), 4, "mail2@", true, StatusWisher.IS_CONSIDERED.getId());
        var listWisher = List.of(wisherDto0, wisherDto1);
        String token = "12345";
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(urlWishers + "dto/" + interviewId)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(WisherDto.class)).thenReturn(Mono.just(new ResponseEntity<>(listWisher, HttpStatus.OK)));
        var actual = wisherService.getAllWisherDtoByInterviewId(token, interviewId);
        assertThat(actual).isEqualTo(listWisher);
    }

    @Test
    void whenGetAllWisherByInterviewIdThenReturnEmptyList() {
        var interviewId = "2";
        String token = "12345";
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(urlWishers + "dto/" + interviewId)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(WisherDto.class)).thenReturn(Mono.empty());
        var actual = wisherService.getAllWisherDtoByInterviewId(token, interviewId);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenGetAllWisherThenListWisherDto() {
        var wisherDto0 = new WisherDto(1, 1, 3, "mail1@", true, StatusWisher.IS_CONSIDERED.getId());
        var wisherDto1 = new WisherDto(1, 2, 4, "mail2@", true, StatusWisher.IS_CONSIDERED.getId());
        var listWisher = List.of(wisherDto0, wisherDto1);
        String token = "12345";
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(urlWishers + "dto/")).thenReturn(requestHeadersMock);
        when(requestHeadersMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(WisherDto.class)).thenReturn(Mono.just(new ResponseEntity<>(listWisher, HttpStatus.OK)));
        var actual = wisherService.getAllWisherDtoByInterviewId(token, "");
        assertThat(actual).isEqualTo(listWisher);
    }

    @Test
    void whenGetAllWisherThenReturnEmptyList() {
        String token = "12345";
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(urlWishers + "dto/")).thenReturn(requestHeadersMock);
        when(requestHeadersMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(WisherDto.class)).thenReturn(Mono.empty());
        var actual = wisherService.getAllWisherDtoByInterviewId(token, "");
        assertThat(actual).isEmpty();
    }

    @Test
    void whenIsWisherThenReturnTrue() {
        var userId = 77;
        var interviewId = 33;
        var wishers = List.of(new WisherDto(1, interviewId, -1, "mail.1", true, StatusWisher.IS_CONSIDERED.getId()),
                new WisherDto(2, interviewId, userId, "mail.1", true, StatusWisher.IS_CONSIDERED.getId()));
        var actual = wisherService.isWisher(userId, interviewId, wishers);
        assertThat(actual).isTrue();
    }

    @Test
    void whenIsWisherThenReturnFalse() {
        var userId = 77;
        var interviewId = 33;
        var wishers = List.of(new WisherDto(1, -1, -1, "mail.1", true, StatusWisher.IS_CONSIDERED.getId()),
                new WisherDto(2, -1, -1, "mail.1", true, StatusWisher.IS_CONSIDERED.getId()));
        var actual = wisherService.isWisher(userId, interviewId, wishers);
        assertThat(actual).isFalse();
    }

    @Test
    void whenTest1GetInterviewStatisticThenReturnEqualsTrue() {
        var wisher1Interview1 = new WisherDto(1, 1, 2, null, false, StatusWisher.IS_CONSIDERED.getId());
        var wisher2Interview1 = new WisherDto(2, 1, 3, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wisher3Interview2 = new WisherDto(3, 2, 4, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wisher4Interview2 = new WisherDto(4, 2, 5, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wishers = List.of(wisher1Interview1, wisher2Interview1, wisher3Interview2, wisher4Interview2);
        var key1 = wisher1Interview1.getInterviewId();
        var key2 = wisher3Interview2.getInterviewId();
        var expected = Map.of(
                key1, new InterviewStatistic(2, 1, 1),
                key2, new InterviewStatistic(2, 0, 2)
        );
        var actual = wisherService.getInterviewStatistic(wishers);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void whenTest2GetInterviewStatisticThenReturnEqualsTrue() {
        var wisher1Interview1 = new WisherDto(1, 1, 2, null, false, StatusWisher.IS_CONSIDERED.getId());
        var wisher2Interview2 = new WisherDto(2, 2, 3, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wisher3Interview3 = new WisherDto(3, 3, 4, null, false, StatusWisher.IS_CONSIDERED.getId());
        var wisher4Interview4 = new WisherDto(4, 4, 5, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wishers = List.of(wisher1Interview1, wisher2Interview2, wisher3Interview3, wisher4Interview4);
        var key1 = wisher1Interview1.getInterviewId();
        var key2 = wisher2Interview2.getInterviewId();
        var key3 = wisher3Interview3.getInterviewId();
        var key4 = wisher4Interview4.getInterviewId();
        var expected = Map.of(
                key1, new InterviewStatistic(1, 1, 0),
                key2, new InterviewStatistic(1, 0, 1),
                key3, new InterviewStatistic(1, 1, 0),
                key4, new InterviewStatistic(1, 0, 1)
        );
        var actual = wisherService.getInterviewStatistic(wishers);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void whenIsDismissedThenTrue() {
        var interviewID = 1;
        var wisher1 = new WisherDto(1, interviewID, 2, null, false, StatusWisher.IS_CONSIDERED.getId());
        var wisher2 = new WisherDto(2, interviewID, 3, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wisher3 = new WisherDto(3, interviewID, 4, null, false, StatusWisher.IS_DISMISSED.getId());
        var wisher4 = new WisherDto(4, interviewID, 5, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wishers = List.of(wisher1, wisher2, wisher3, wisher4);
        var actual = wisherService.isDismissed(interviewID, wishers);
        assertThat(actual).isTrue();
    }

    @Test
    void whenIsDismissedThenFalse() {
        var interviewID = 1;
        var wisher1 = new WisherDto(1, interviewID, 2, null, false, StatusWisher.IS_CONSIDERED.getId());
        var wisher2 = new WisherDto(2, interviewID, 3, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wisher3 = new WisherDto(3, interviewID, 4, null, false, StatusWisher.IS_COMPLETED.getId());
        var wisher4 = new WisherDto(4, interviewID, 5, null, true, StatusWisher.IS_CONSIDERED.getId());
        var wishers = List.of(wisher1, wisher2, wisher3, wisher4);
        var actual = wisherService.isDismissed(interviewID, wishers);
        assertThat(actual).isFalse();
    }

    @Test
    void whenSetNewStatusByWisherInterviewThenTrue() {
        WisherDto wisherDto = new WisherDto(1, 1, 1,
                "mail@mail.ru", true,
                StatusWisher.IS_CONSIDERED.getId());
        String token = "12345";
        var interviewId = String.valueOf(wisherDto.getInterviewId());
        var wisherId = String.valueOf(wisherDto.getId());
        var newStatusId = String.valueOf(StatusWisher.IS_DISMISSED.getId());
        var anyStatusId = String.valueOf(StatusWisher.IS_REJECTED.getId());
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("interviewId", interviewId);
        param.add("wisherId", wisherId);
        param.add("newStatusId", newStatusId);
        param.add("anyStatusId", anyStatusId);
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(urlWishers + "status/")).thenReturn(requestBodyMock);
        when(requestBodyMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(param)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(HttpStatus.class)).thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.OK)));
        var setStatusActual = wisherService.setNewStatusByWisherInterview(token, interviewId,
                wisherId, newStatusId, anyStatusId);
        assertThat(setStatusActual).isTrue();
    }

    @Test
    void whenSetNewStatusByWisherInterviewThenFalse() {
        WisherDto wisherDto = new WisherDto(1, 1, 1,
                "mail@mail.ru", true,
                StatusWisher.IS_CONSIDERED.getId());
        String token = "12345";
        var interviewId = String.valueOf(wisherDto.getInterviewId());
        var wisherId = String.valueOf(wisherDto.getId());
        var newStatusId = String.valueOf(StatusWisher.IS_DISMISSED.getId());
        var anyStatusId = String.valueOf(StatusWisher.IS_REJECTED.getId());
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("interviewId", interviewId);
        param.add("wisherId", wisherId);
        param.add("newStatusId", newStatusId);
        param.add("anyStatusId", anyStatusId);
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(urlWishers + "status/")).thenReturn(requestBodyMock);
        when(requestBodyMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(param)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(HttpStatus.class)).thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
        var setStatusActual = wisherService.setNewStatusByWisherInterview(token, interviewId,
                wisherId, newStatusId, anyStatusId);
        assertThat(setStatusActual).isFalse();
    }
}