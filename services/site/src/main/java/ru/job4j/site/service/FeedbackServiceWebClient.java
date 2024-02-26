package ru.job4j.site.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.job4j.site.domain.StatusInterview;
import ru.job4j.site.dto.FeedbackDTO;
import ru.job4j.site.dto.InterviewDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FeedbackServiceWebClient
 * Класс реализует получение и обработку сущности Feedback из сервиса mock.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
@Service
@Slf4j
public class FeedbackServiceWebClient implements FeedbackService {
    private final String urlMock;
    private WebClient webClientFeedback;
    private static final String URL_FEEDBACK = "/feedback/";

    private InterviewService interviewService;

    public FeedbackServiceWebClient(@Value("${service.mock}") String urlMock, InterviewService interviewService) {
        this.urlMock = urlMock;
        this.webClientFeedback = WebClient.create(this.urlMock);
        this.interviewService = interviewService;
    }

    /**
     * Метод сохраняет отзыв о собеседовании FeedbackDTO
     * Если статус сохранен успешно то обновляется статус собеседования.
     *
     * @param token       Authorization token
     * @param feedbackDTO FeedbackDTO
     * @return boolean true/false
     */
    @Override
    public boolean save(String token, FeedbackDTO feedbackDTO) {
        var interview = new InterviewDTO();
        try {
            interview = interviewService.getById(token, feedbackDTO.getInterviewId());
            var roleInInterview = gerRoleInInterview(feedbackDTO.getUserId(), interview);
            feedbackDTO.setRoleInInterview(roleInInterview);
        } catch (Exception e) {
            log.error("InterviewService.class method getById FROM API MOCK service error: {}", e.getMessage());
            return false;
        }
        var responseEntityMono = this.webClientFeedback
                .post()
                .uri(URL_FEEDBACK)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(feedbackDTO)
                .retrieve()
                .toEntity(FeedbackDTO.class)
                .doOnError(err -> log.error("API MOCK not found: {}", err.getMessage()))
                .blockOptional();
        var result = responseEntityMono
                .map(re -> re.getStatusCode().is2xxSuccessful())
                .orElse(false);
        if (result) {
            updateStatusInterview(token, interview, feedbackDTO.getUserId());
        }
        return result;
    }

    /**
     * Метод возвращает все Отзывы по Interview ID
     *
     * @param interviewId ID
     * @return List<FeedbackDTO>
     */
    @Override
    public List<FeedbackDTO> findByInterviewId(int interviewId) {
        Optional<ResponseEntity<List<FeedbackDTO>>> listResponseEntity = this.webClientFeedback
                .get()
                .uri(URL_FEEDBACK + interviewId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(FeedbackDTO.class)
                .doOnError(err -> log.error("API MOCK not found: {}", err.getMessage()))
                .blockOptional();
        return listResponseEntity
                .map(HttpEntity::getBody)
                .orElse(new ArrayList<>());
    }

    /**
     * Метод проверяет, оставил пользователь отзыв на собеседование.
     *
     * @param interviewId int ID Interview
     * @param userID      int ID User
     * @return boolean true/false
     */
    @Override
    public List<FeedbackDTO> findByInterviewIdAndUserId(int interviewId, int userID) {
        var uri = String.format("%s?iId=%d&uId=%d", URL_FEEDBACK, interviewId, userID);
        Optional<ResponseEntity<List<FeedbackDTO>>> listResponseEntity = this.webClientFeedback
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(FeedbackDTO.class)
                .doOnError(err -> log.error("API MOCK not found: {}", err.getMessage()))
                .blockOptional();
        return listResponseEntity
                .map(HttpEntity::getBody)
                .orElse(new ArrayList<>());
    }

    /**
     * Метод возвращает роль участника в собеседовании,
     * в зависимости от статуса собеседования.
     *
     * @param userId       int ID User
     * @param interviewDTO InterviewDto
     * @return roleInInterview.
     */
    public int gerRoleInInterview(int userId, InterviewDTO interviewDTO) {
        var roleInInterview = interviewDTO.getMode();
        if (userId != interviewDTO.getSubmitterId()) {
            roleInInterview = interviewDTO.getMode() == 1 ? 2 : 1;
        }
        return roleInInterview;
    }

    /**
     * Метод меняет статус интервью в зависимости от текущего статуса.
     * Если у пользователя только один отзыв (который был успешно сохранен перед этим методом),
     * и текущий статус IN_FEEDBACK, то устанавливается статус IS_COMPLETED
     * Если текущий статус IN_PROGRESS, то устанавливается статус IS_FEEDBACK
     * <p>
     * Примечание: в начале сохраняется отзыв метод SAVE,
     * и если он был успешно сохранен то вызывается метод, изменения статуса updateStatusInterview.
     *
     * @param token        security User token
     * @param interview    InterviewDTO
     * @param feedbackUser ID User author feedback
     * @return int ID new Status OR ID old Status
     */
    public int updateStatusInterview(String token, InterviewDTO interview, int feedbackUser) {
        var feedbacksByUser = findByInterviewIdAndUserId(interview.getId(), feedbackUser);
        if (feedbacksByUser.size() == 1 && StatusInterview.IS_FEEDBACK.getId() == interview.getStatus()) {
            interviewService.updateStatus(token, interview.getId(), StatusInterview.IS_COMPLETED.getId());
            return StatusInterview.IS_COMPLETED.getId();
        } else if (StatusInterview.IN_PROGRESS.getId() == interview.getStatus()) {
            interviewService.updateStatus(token, interview.getId(), StatusInterview.IS_FEEDBACK.getId());
            return StatusInterview.IS_FEEDBACK.getId();
        }
        return interview.getStatus();
    }

    public void setWebClientFeedback(WebClient webClient) {
        this.webClientFeedback = webClient;
    }
}
