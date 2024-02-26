package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.FeedbackDTO;
import ru.job4j.site.dto.InterviewDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * CheckDev пробное собеседование
 *
 * @author Dmitry Stepanov
 * @version 29.10.2023 03:14
 */
@SpringBootTest(classes = FeedbackServiceWebClient.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class FeedbackServiceWebClientTest {

    private static final String URL = "http://testurl:1005000";
    private final String urlFeedback = "/feedback/";

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
    @MockBean
    private InterviewService interviewService;

    private FeedbackServiceWebClient feedbackService;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackServiceWebClient(URL, interviewService);
        feedbackService.setWebClientFeedback(webClientMock);
    }

    @Test
    void initInjectedNotNul() {
        assertThat(interviewService).isNotNull();
        assertThat(feedbackService).isNotNull();
    }

    @Test
    void whenSaveThenReturnTrue() throws JsonProcessingException {
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setMode(1);
        var feedbackDto1 = new FeedbackDTO(1, interviewDto.getId(), interviewDto.getSubmitterId(), 0, "text", 5);
        var token = "1234";
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.empty());
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(urlFeedback)).thenReturn(requestBodyMock);
        when(requestBodyMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(feedbackDto1)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(FeedbackDTO.class)).thenReturn(Mono.just(new ResponseEntity<>(feedbackDto1, HttpStatus.CREATED)));
        when(interviewService.getById(token, feedbackDto1.getInterviewId())).thenReturn(interviewDto);
        doNothing().when(interviewService).updateStatus(token, interviewDto.getId(), StatusInterview.IS_FEEDBACK.getId());
        var actual = feedbackService.save(token, feedbackDto1);
        assertThat(actual).isTrue();
    }

    @Test
    void whenSaveFeedbackThenReturnFalse() throws JsonProcessingException {
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setMode(1);
        var feedbackDto1 = new FeedbackDTO(1, interviewDto.getId(), interviewDto.getSubmitterId(), 0, "text", 5);
        var token = "1234";
        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(urlFeedback)).thenReturn(requestBodyMock);
        when(requestBodyMock.header("Authorization", "Bearer " + token)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(feedbackDto1)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(FeedbackDTO.class)).thenReturn(Mono.empty());
        when(interviewService.getById(token, feedbackDto1.getInterviewId())).thenReturn(interviewDto);
        var actual = feedbackService.save(token, feedbackDto1);
        assertThat(actual).isFalse();
    }

    @Test
    void whenFindFeedbackByInterviewIdThenReturnListFeedBackDto() {
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        var feedbackDto2 = new FeedbackDTO(2, 1, 2, 2, "text2", 5);
        var feedbacks = List.of(feedbackDto1, feedbackDto2);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(urlFeedback + feedbackDto1.getInterviewId())).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.just(new ResponseEntity<>(feedbacks, HttpStatus.OK)));
        var actual = feedbackService.findByInterviewId(feedbackDto1.getInterviewId());
        assertThat(actual).isEqualTo(feedbacks);
    }

    @Test
    void whenFindFeedbackByInterviewIdThenReturnEmptyList() {
        var interviewId = 1;
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(urlFeedback + interviewId)).thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.empty());
        var actual = feedbackService.findByInterviewId(interviewId);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindByInterviewIdAndUserIdThenReturnFeedbackDtoList() {
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        var feedbackDto2 = new FeedbackDTO(2, 1, 1, 2, "text2", 5);
        var feedbacks = List.of(feedbackDto1, feedbackDto2);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.just(new ResponseEntity<>(feedbacks, HttpStatus.OK)));
        var actual = feedbackService.findByInterviewIdAndUserId(feedbackDto1.getInterviewId(), feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(feedbacks);
    }

    @Test
    void whenFindByInterviewIdAndUserIdThenReturnEmptyList() {
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.empty());
        var actual = feedbackService.findByInterviewIdAndUserId(feedbackDto1.getInterviewId(), feedbackDto1.getUserId());
        assertThat(actual).isEmpty();
    }

    @Test
    void whenGerRoleInInterviewThenReturnInterviewMode() {
        var interview = new InterviewDTO();
        interview.setSubmitterId(1);
        interview.setMode(1);
        var userId = interview.getSubmitterId();
        var expected = interview.getMode();
        var actual = feedbackService.gerRoleInInterview(userId, interview);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenGerRoleInInterviewThenReturnRoleTwo() {
        var interview = new InterviewDTO();
        interview.setSubmitterId(1);
        interview.setMode(1);
        var userId = 2;
        var expected = 2;
        var actual = feedbackService.gerRoleInInterview(userId, interview);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenGerRoleInInterviewThenReturnRoleOne() {
        var interview = new InterviewDTO();
        interview.setSubmitterId(1);
        interview.setMode(2);
        var userId = 2;
        var expected = 1;
        var actual = feedbackService.gerRoleInInterview(userId, interview);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenUpdateStatusInterviewThenReturnIDStatusIsFeedback() {
        var token = "1234";
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setStatus(StatusInterview.IN_PROGRESS.getId());
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        doNothing().when(interviewService)
                .updateStatus(token, interviewDto.getId(), StatusInterview.IS_FEEDBACK.getId());
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.empty());
        var actual = feedbackService.updateStatusInterview(token, interviewDto, feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(StatusInterview.IS_FEEDBACK.getId());
    }

    @Test
    void whenUpdateStatusInterviewThenReturnIdOldStatus() {
        var token = "1234";
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setStatus(StatusInterview.IS_NEW.getId());
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        doNothing().when(interviewService)
                .updateStatus(token, interviewDto.getId(), StatusInterview.IS_FEEDBACK.getId());
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.empty());
        var actual = feedbackService.updateStatusInterview(token, interviewDto, feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(interviewDto.getStatus());
    }

    @Test
    void whenUpdateStatusInterviewIsFeedbackThenReturnIdStatusFeedback() {
        var token = "1234";
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setStatus(StatusInterview.IS_FEEDBACK.getId());
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        doNothing().when(interviewService)
                .updateStatus(token, interviewDto.getId(), StatusInterview.IS_FEEDBACK.getId());
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.empty());
        var actual = feedbackService.updateStatusInterview(token, interviewDto, feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(StatusInterview.IS_FEEDBACK.getId());
    }

    @Test
    void whenUpdateStatusInterviewIsFeedbackAndFeedbackTwoThenReturnIdStatusFeedback() {
        var token = "1234";
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setStatus(StatusInterview.IS_FEEDBACK.getId());
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        var feedbackDto2 = new FeedbackDTO(2, 1, 1, 2, "text2", 5);
        var feedbacks = List.of(feedbackDto1, feedbackDto2);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.just(new ResponseEntity<>(feedbacks, HttpStatus.OK)));
        var actual = feedbackService.updateStatusInterview(token, interviewDto, feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(StatusInterview.IS_FEEDBACK.getId());
    }

    @Test
    void whenUpdateStatusInterviewIsFeedbackThenReturnIdStatusCompleted() {
        var token = "1234";
        var interviewDto = new InterviewDTO();
        interviewDto.setId(1);
        interviewDto.setSubmitterId(1);
        interviewDto.setStatus(StatusInterview.IS_FEEDBACK.getId());
        var feedbackDto1 = new FeedbackDTO(1, 1, 1, 1, "text", 5);
        var feedbacks = List.of(feedbackDto1);
        doNothing().when(interviewService)
                .updateStatus(token, interviewDto.getId(), StatusInterview.IS_COMPLETED.getId());
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock
                .uri(
                        String.format("%s?iId=%d&uId=%d",
                                urlFeedback, feedbackDto1.getInterviewId(), feedbackDto1.getUserId())))
                .thenReturn(requestBodyMock);
        when(requestBodyMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntityList(FeedbackDTO.class)).thenReturn(Mono.just(new ResponseEntity<>(feedbacks, HttpStatus.OK)));
        var actual = feedbackService.updateStatusInterview(token, interviewDto, feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(StatusInterview.IS_COMPLETED.getId());
    }
}