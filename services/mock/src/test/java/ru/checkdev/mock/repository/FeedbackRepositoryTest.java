package ru.checkdev.mock.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Feedback;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.mapper.FeedbackMapper;

import javax.persistence.EntityManager;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * FeedbackRepository test
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
@DataJpaTest
@RunWith(SpringRunner.class)
class FeedbackRepositoryTest {
    private Interview interview;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private FeedbackRepository repository;

    @BeforeEach
    public void initTable() {
        interview = new Interview();
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("mail@mail");
        interview.setApproximateDate("approximate");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        entityManager.createQuery("DELETE FROM cd_feedback").executeUpdate();
        entityManager.persist(interview);
        entityManager.clear();
    }

    @Test
    public void injectedComponentAreNotNull() {
        assertThat(entityManager).isNotNull();
        assertThat(repository).isNotNull();
    }

    @Test
    void thenFindAllByInterviewIdWhenReturnList() {
        var feedback1 = new Feedback(0, interview, 1, 2, "text1", 4);
        var feedback2 = new Feedback(0, interview, 2, 1, "text2", 3);
        entityManager.persist(feedback1);
        entityManager.persist(feedback2);
        var feedbackDto1 = FeedbackMapper.getFeedbackDTO(feedback1);
        var feedbackDto2 = FeedbackMapper.getFeedbackDTO(feedback2);
        var expectedList = List.of(feedbackDto1, feedbackDto2);
        var actualList = repository.findAllByInterviewId(interview.getId());
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void thenFindAllByInterviewIdWhenReturnEmptyList() {
        var actualList = repository.findAllByInterviewId(interview.getId());
        assertThat(actualList).isEmpty();
    }

    @Test
    void thenFindByInterviewIdAndUserIdThenReturnFeedbackDTO() {
        var feedback1 = new Feedback(0, interview, 1, 2, "text1", 4);
        entityManager.persist(feedback1);
        entityManager.clear();
        var feedbackDto1 = FeedbackMapper.getFeedbackDTO(feedback1);
        var actual = repository.findByInterviewIdAndUserId(feedbackDto1.getInterviewId(), feedbackDto1.getUserId());
        assertThat(actual).isEqualTo(List.of(feedbackDto1));
    }

    @Test
    void thenFindByInterviewIdAndUserIdThenReturnNull() {
        var feedback1 = new Feedback(0, interview, 1, 2, "text1", 4);
        var actual = repository.findByInterviewIdAndUserId(feedback1.getInterview().getId(), feedback1.getUserId());
        assertThat(actual).isEmpty();
    }
}