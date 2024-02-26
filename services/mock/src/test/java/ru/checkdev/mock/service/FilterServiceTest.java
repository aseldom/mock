package ru.checkdev.mock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.MockSrv;
import ru.checkdev.mock.domain.Filter;
import ru.checkdev.mock.repository.FilterRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MockSrv.class)
@RunWith(SpringRunner.class)
public class FilterServiceTest {

    @MockBean
    private FilterRepository filterRepository;

    @Autowired
    private FilterService filterService;

    @Test
    public void whenFilterSaved() {
        var filter = new Filter(1, 1, 1);
        when(filterRepository.save(filter)).thenReturn(filter);
        assertThat(filterService.save(filter), is(Optional.of(filter)));
    }

    @Test
    public void whenFilterFindByUserId() {
        var filter = new Filter(1, 1, 1);
        when(filterRepository.getByUserId(1)).thenReturn(Optional.of(filter));
        assertThat(filterService.findByUserId(1), is(Optional.of(filter)));
    }

    @Test
    public void whenFilterCanNotFound() {
        assertThat(filterService.findByUserId(-1), is(Optional.empty()));
    }

    @Test
    public void whenFilterDeletedByUserId() {
        var filter = new Filter(1, 1, 1);
        when(filterRepository.deleteByUserId(1)).thenReturn(1);
        assertThat(filterService.deleteByUserId(1), is(1));
    }
}
