package ru.checkdev.mock.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Feedback;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.dto.FeedbackDTO;
import ru.checkdev.mock.mapper.FeedbackMapper;
import ru.checkdev.mock.repository.FeedbackRepository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * FeedbackJpaService TEST
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 25.10.2023
 */
@SpringBootTest(classes = FeedbackJpaService.class)
@RunWith(SpringRunner.class)
class FeedbackJpaServiceTest {
    private final Interview interview = Interview.of()
            .mode(1)
            .submitterId(1)
            .title("title")
            .additional("additional")
            .contactBy("mail@mail")
            .approximateDate("approximate")
            .createDate(new Timestamp(System.currentTimeMillis()))
            .build();

    @MockBean
    private FeedbackRepository repository;
    @Autowired
    private FeedbackJpaService service;

    @Test
    void injectedComponentAreNotNull() {
        assertThat(repository).isNotNull();
        assertThat(service).isNotNull();
    }

    @Test
    void whenSaveThenReturnOptionalFeedbackDTO() {
        var feedback1 = new Feedback(1, interview, 1, 2, "text1", 4);
        when(repository.save(feedback1)).thenReturn(feedback1);
        var expected = FeedbackMapper.getFeedbackDTO(feedback1);
        var actual = service.save(expected);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void whenSaveThenReturnOptionalEmpty() {
        var feedback1 = new Feedback(1, interview, 1, 2, "text1", 4);
        when(repository.save(feedback1)).thenThrow(new RuntimeException("error"));
        var feedbackDto = FeedbackMapper.getFeedbackDTO(feedback1);
        var actual = service.save(feedbackDto);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindByInterviewIdThenReturnListDTO() {
        var feedback1 = new Feedback(0, interview, 1, 2, "text1", 4);
        var feedback2 = new Feedback(0, interview, 2, 1, "text2", 3);
        var feedbackDTO1 = FeedbackMapper.getFeedbackDTO(feedback1);
        var feedbackDTO2 = FeedbackMapper.getFeedbackDTO(feedback2);
        var expectedList = List.of(feedbackDTO1, feedbackDTO2);
        when(repository.findAllByInterviewId(interview.getId())).thenReturn(expectedList);
        var actualList = service.findByInterviewId(interview.getId());
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    void whenFindByInterviewIdThenReturnEmptyList() {
        when(repository.findAllByInterviewId(interview.getId())).thenReturn(Collections.emptyList());
        var actual = service.findByInterviewId(interview.getId());
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindByInterviewIdAndUserIdThenReturnOptionalFeedbackDTO() {
        var feedback1 = new FeedbackDTO(1, interview.getId(), 1, 2, "text1", 4);
        when(repository.findByInterviewIdAndUserId(feedback1.getInterviewId(), feedback1.getUserId()))
                .thenReturn(List.of(feedback1));
        var actual = service.findByInterviewIdAndUserId(feedback1.getInterviewId(), feedback1.getUserId());
        assertThat(actual).isEqualTo(List.of(feedback1));
    }

    @Test
    void whenFindByInterviewIdAndUserIdThenReturnOptionalEmpty() {
        var feedback1 = new FeedbackDTO(1, interview.getId(), 1, 2, "text1", 4);
        when(repository.findByInterviewIdAndUserId(feedback1.getInterviewId(), feedback1.getUserId()))
                .thenReturn(Collections.emptyList());
        var actual = service.findByInterviewIdAndUserId(feedback1.getInterviewId(), feedback1.getUserId());
        assertThat(actual).isEmpty();
    }

}