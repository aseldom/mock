package ru.checkdev.mock.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.repository.InterviewRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MockSrv.class)
@RunWith(SpringRunner.class)
class InterviewServiceTest {

    @MockBean
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewService interviewService;

    private Interview interview = Interview.of()
            .id(1)
            .mode(2)
            .status(1)
            .submitterId(3)
            .title("test_title")
            .additional("test_additional")
            .contactBy("test_contact_by")
            .approximateDate("test_approximate_date")
            .createDate(new Timestamp(System.currentTimeMillis()))
            .build();

    @Test
    public void whenSaveAndGetTheSame() {
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);
        var actual = interviewService.save(interview);
        assertThat(actual, is(Optional.of(interview)));
    }

    @Test
    public void whenSaveAndGetEmpty() {
        when(interviewRepository.save(any(Interview.class))).thenThrow(new DataIntegrityViolationException(""));
        var actual = interviewService.save(interview);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void whenGetAll() {
        List<Interview> interviews = IntStream.range(0, 5).mapToObj(i -> {
            var interview = new Interview();
            interview.setId(i);
            interview.setMode(1);
            interview.setSubmitterId(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional("Some text");
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            return interview;
        }).toList();
        var page = new PageImpl<>(interviews);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"));
        when(interviewRepository.findAll(pageable)).thenReturn(page);
        var actual = interviewService.findPaging(0, 5);
        assertThat(actual, is(page));
    }

    @Test
    public void whenFindByIdIsCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        var actual = interviewService.findById(1);
        assertThat(actual, is(Optional.of(interview)));
    }

    @Test
    public void whenFindByIdIsNotCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        var actual = interviewService.findById(1);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void whenUpdateIsCorrect() {
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);
        var actual = interviewService.save(interview);
        assertThat(actual, is(Optional.of(interview)));
    }

    @Test
    public void whenDeleteIsCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.of(interview));
        var actual = interviewService.delete(interview);
        assertThat(actual, is(true));
    }

    @Test
    public void whenDeleteIsNotCorrect() {
        when(interviewRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        var actual = interviewService.delete(interview);
        assertThat(actual, is(false));
    }

    @Test
    public void whenGetAllWithTopicIdIsNull() {
        Interview interviewWithTopicId = interview;
        interviewWithTopicId.setTopicId(1);
        when(interviewRepository.findAll()).thenReturn(List.of(interviewWithTopicId));
        var actual = interviewService.findAll();
        assertThat(actual, is(List.of(interviewWithTopicId)));
    }

    @Test
    public void whenFindByTypeAllWithTopicIdIsNull() {
        Interview interviewWithTopicId = interview;
        interviewWithTopicId.setTopicId(1);
        interviewWithTopicId.setMode(1);
        when(interviewRepository.findByMode(1)).thenReturn(List.of(interviewWithTopicId));
        var actual = interviewService.findByMode(1);
        assertThat(actual, is(List.of(interviewWithTopicId)));
    }

    @Test
    public void whenUpdateStatusThenTrue() {
        doNothing().when(interviewRepository).updateStatus(1, 12);
        var actual = interviewService.updateStatus(1, 12);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenGetByTopicsIds() {
        List<Interview> oddTopicIdsInterviewList = new ArrayList<>();
        List<Interview> evenTopicIdsInterviewList = new ArrayList<>();
        IntStream.range(0, 8).forEach(i -> {
            var interview = new Interview();
            interview.setMode(1);
            interview.setSubmitterId(1);
            interview.setTitle(String.format("Interview_%d", i));
            interview.setAdditional(String.format("Some text_%d", i));
            interview.setContactBy("Some contact");
            interview.setApproximateDate("30.02.2024");
            interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
            interview.setTopicId(i + 1);
            if (interview.getTopicId() % 2 == 0) {
                evenTopicIdsInterviewList.add(interview);
            } else {
                oddTopicIdsInterviewList.add(interview);
            }
        });
        var evenPage = new PageImpl<>(evenTopicIdsInterviewList);
        var oddPage = new PageImpl<>(oddTopicIdsInterviewList);
        when(interviewRepository.findByTopicIdIn(List.of(2, 4, 6), PageRequest.of(0, 5)))
                .thenReturn(evenPage);
        when(interviewRepository.findByTopicIdIn(List.of(1, 3, 5, 7), PageRequest.of(0, 5)))
                .thenReturn(oddPage);
        assertThat(interviewService.findByTopicsIds(List.of(2, 4, 6), 0, 5), is(evenPage));
        assertThat(interviewService.findByTopicsIds(List.of(1, 3, 5, 7), 0, 5), is(oddPage));
    }
}