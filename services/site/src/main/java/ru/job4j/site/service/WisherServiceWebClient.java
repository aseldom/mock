package ru.job4j.site.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import ru.job4j.site.domain.StatusWisher;
import ru.job4j.site.dto.InterviewStatistic;
import ru.job4j.site.dto.WisherDto;

import java.util.*;

/**
 * WisherServiceWebClient
 * Класс реализует получение и обработку модели WisherDTO из сервиса mock
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 11.10.2023
 */
@Service
@Slf4j
public class WisherServiceWebClient implements WisherService {
    private final String urlMock;
    private WebClient webClientWisher;
    private static final String URL_WISHER = "/wisher/";
    private static final String URL_WISHERS = "/wishers/";


    public WisherServiceWebClient(@Value("${service.mock}") String urlMock) {
        this.urlMock = urlMock;
        this.webClientWisher = WebClient.create(this.urlMock);
    }

    /**
     * Метод сохраняет участника WisherDTO
     *
     * @param token     String
     * @param wisherDto WisherDto.class
     * @return boolean true/false
     */
    @Override
    public boolean saveWisherDto(String token, WisherDto wisherDto) {
        var responseEntityMono = this.webClientWisher
                .post()
                .uri(URL_WISHER)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(wisherDto)
                .retrieve()
                .toEntity(WisherDto.class)
                .doOnError(err -> log.error("API MOCK not found: {}", err.getMessage()))
                .blockOptional();
        return responseEntityMono
                .map(re -> re.getStatusCode().is2xxSuccessful())
                .orElse(false);
    }

    /**
     * Метод возвращает всех участников собеседования interviewId
     * Если interviewId == "" вернется список всех участников по всем собеседованиям.
     *
     * @param token       String
     * @param interviewId String
     * @return List<WisherDto>
     */
    @Override
    public List<WisherDto> getAllWisherDtoByInterviewId(String token, String interviewId) {
        Optional<ResponseEntity<List<WisherDto>>> listResponseEntity = this.webClientWisher
                .get()
                .uri(URL_WISHERS + "dto/" + interviewId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(WisherDto.class)
                .doOnError(err -> log.error("API MOCK not found: {}", err.getMessage()))
                .blockOptional();
        return listResponseEntity
                .map(HttpEntity::getBody)
                .orElse(new ArrayList<>());
    }

    /**
     * Метод по устанавливает новый статус участниках интервью
     *
     * @param token       User token
     * @param interviewId ID Interview
     * @param wisherId    ID select Wisher
     * @param newStatusId new Status ID select Wisher
     * @param anyStatusId new Status ID any Wisher
     * @return boolean true / false
     */
    @Override
    public boolean setNewStatusByWisherInterview(String token, String interviewId,
                                                 String wisherId, String newStatusId,
                                                 String anyStatusId) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("interviewId", interviewId);
        param.add("wisherId", wisherId);
        param.add("newStatusId", newStatusId);
        param.add("anyStatusId", anyStatusId);
        var setNewStatus = this.webClientWisher
                .post()
                .uri(URL_WISHERS + "status/")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(param)
                .retrieve()
                .toEntity(HttpStatus.class)
                .doOnError(err -> log.error("API MOCK not found: {}", err.getMessage()))
                .blockOptional();
        return setNewStatus
                .map(re -> re.getStatusCode().is2xxSuccessful())
                .orElse(false);
    }

    /**
     * Метод проверяет, является пользователь участником конкретного интервью
     *
     * @param userId      User ID
     * @param interviewId Interview ID
     * @param wishers     List<WisherDto>
     * @return boolean true/false
     */
    @Override
    public boolean isWisher(int userId, int interviewId, List<WisherDto> wishers) {
        return wishers.stream()
                .anyMatch(wiser ->
                        wiser.getUserId() == userId
                                && wiser.getInterviewId() == interviewId);
    }

    /**
     * Метод проверяет одобрен хотя бы один участник собеседования.
     *
     * @param interviewId Interview ID
     * @param wishers     List<WisherDto>
     * @return boolean true or false
     */
    @Override
    public boolean isDismissed(int interviewId, List<WisherDto> wishers) {
        int dismissedId = StatusWisher.IS_DISMISSED.getId();
        return wishers.stream()
                .filter(w -> w.getInterviewId() == interviewId)
                .mapToInt(WisherDto::getStatus)
                .anyMatch(s -> s == dismissedId);
    }

    /**
     * Метод собирает статистику по всем коллекциям в карту
     * Map<Integer, InterviewStatistic>
     * key = interviewId,
     * value = InterviewStatistic calc.
     *
     * @param wishers List<WisherDTO>
     * @return Map<Integer, InterviewStatistic>
     */
    @Override
    public Map<Integer, InterviewStatistic> getInterviewStatistic(List<WisherDto> wishers) {
        Map<Integer, InterviewStatistic> result = new HashMap<>();
        for (WisherDto wisherDto : wishers) {
            var interviewId = wisherDto.getInterviewId();
            int participate = 1;
            int except = wisherDto.isApprove() ? 0 : 1;
            int passed = wisherDto.isApprove() ? 1 : 0;
            result.computeIfPresent(interviewId,
                    (key, oldValue) -> new InterviewStatistic(
                            oldValue.getParticipate() + participate,
                            oldValue.getExpect() + except,
                            oldValue.getPassed() + passed));
            result.putIfAbsent(interviewId, new InterviewStatistic(participate, except, passed));
        }
        return result;
    }

    /**
     * Метод для установки своего WebClient
     *
     * @param webClientWisher WebClient
     */
    public void setWebClientWisher(WebClient webClientWisher) {
        this.webClientWisher = webClientWisher;
    }
}
