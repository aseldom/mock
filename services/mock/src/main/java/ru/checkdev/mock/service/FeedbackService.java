package ru.checkdev.mock.service;

import ru.checkdev.mock.dto.FeedbackDTO;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackService интерфейс описывает поведения слоя бизнес логики,
 * обработки DTO и DAO моделей сущности Feedback.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
public interface FeedbackService {

    Optional<FeedbackDTO> save(FeedbackDTO feedbackDTO);

    List<FeedbackDTO> findByInterviewId(int interviewId);

    List<FeedbackDTO> findByInterviewIdAndUserId(int interviewId, int userId);
}
