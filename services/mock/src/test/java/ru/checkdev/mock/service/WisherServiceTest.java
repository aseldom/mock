package ru.checkdev.mock.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import ru.checkdev.mock.repository.WisherRepository;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockSrv.class)
class WisherServiceTest {

    @MockBean
    private WisherRepository wisherRepository;

    @Autowired
    private WisherService wisherService;

    private Interview interview = Interview.of()
            .id(1)
            .mode(2)
            .submitterId(3)
            .title("test_title")
            .additional("test_additional")
            .contactBy("test_contact_by")
            .approximateDate("test_approximate_date")
            .createDate(new Timestamp(System.currentTimeMillis()))
            .build();

    private Wisher wisher = Wisher.of()
            .id(1)
            .interview(interview)
            .userId(1)
            .contactBy("test_contact_by")
            .approve(true)
            .build();

    @Test
    public void whenSaveAndGetTheSame() {
        when(wisherRepository.save(any(Wisher.class))).thenReturn(wisher);
        var actual = wisherService.save(wisher);
        assertThat(actual, is(Optional.of(wisher)));
    }

    @Test
    public void whenSaveAndGetEmpty() {
        when(wisherRepository.save(any(Wisher.class))).thenThrow(new DataIntegrityViolationException(""));
        var actual = wisherService.save(wisher);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void whenGetAll() {
        when(wisherRepository.findAll()).thenReturn(List.of(wisher));
        var actual = wisherService.findAll();
        assertThat(actual, is(List.of(wisher)));
    }

    @Test
    public void whenFindByIdIsCorrect() {
        when(wisherRepository.findById(any(Integer.class))).thenReturn(Optional.of(wisher));
        var actual = wisherService.findById(1);
        assertThat(actual, is(Optional.of(wisher)));
    }

    @Test
    public void whenFindByIdIsNotCorrect() {
        when(wisherRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        var actual = wisherService.findById(1);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void whenUpdateIsCorrect() {
        when(wisherRepository.save(any(Wisher.class))).thenReturn(wisher);
        var actual = wisherService.save(wisher);
        assertThat(actual, is(Optional.of(wisher)));
    }

    @Test
    public void whenDeleteIsCorrect() {
        when(wisherRepository.findById(any(Integer.class))).thenReturn(Optional.of(wisher));
        var actual = wisherService.delete(wisher);
        assertThat(actual, is(true));
    }

    @Test
    public void whenDeleteIsNotCorrect() {
        when(wisherRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        var actual = wisherService.delete(wisher);
        assertThat(actual, is(false));
    }
}