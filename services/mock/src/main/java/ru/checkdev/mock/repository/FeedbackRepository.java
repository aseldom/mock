package ru.checkdev.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.checkdev.mock.domain.Feedback;
import ru.checkdev.mock.dto.FeedbackDTO;

import java.util.List;

/**
 * Слой persistence для, хранение и получения модели Feedback
 * в базе данных.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    /**
     * Метод возвращает список FeedbackDTO по ID Interview
     *
     * @param interviewId ID Interview
     * @return List<FeedbackDTP>
     */
    @Query("""
            SELECT 
            new ru.checkdev.mock.dto.FeedbackDTO(fe.id, fe.interview.id, fe.userId, fe.roleInInterview, fe.textFeedback, fe.scope) 
            FROM cd_feedback fe 
            WHERE fe.interview.id=:interviewId
            """)
    List<FeedbackDTO> findAllByInterviewId(@Param("interviewId") int interviewId);

    /**
     * Метод возвращает отзыв по пользователю и интервью
     *
     * @param interviewId int ID Interview
     * @param userId      int ID user
     * @return FeedbackDTO
     */
    @Query("""
            SELECT 
            new ru.checkdev.mock.dto.FeedbackDTO(fe.id, fe.interview.id, fe.userId, fe.roleInInterview, fe.textFeedback, fe.scope) 
            FROM cd_feedback fe 
            WHERE fe.interview.id=:interviewId AND fe.userId=:userId
            """)
    List<FeedbackDTO> findByInterviewIdAndUserId(@Param("interviewId") int interviewId,
                                           @Param("userId") int userId);
}
