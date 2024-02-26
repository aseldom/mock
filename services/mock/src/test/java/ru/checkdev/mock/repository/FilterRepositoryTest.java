package ru.checkdev.mock.repository;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Filter;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class FilterRepositoryTest {

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void whenFilterSaved() {
        var filter = new Filter(1, 1, 1);
        entityManager.persist(filter);
        var saved = filterRepository.save(filter);
        assertThat(filter).isEqualTo(saved);
    }

    @Test
    public void whenFilterFindByUserId() {
        var filter = new Filter(1, 1, 1);
        entityManager.persist(filter);
        var optionalFilter = filterRepository.getByUserId(1);
        assertTrue(optionalFilter.isPresent());
        assertThat(filter).isEqualTo(optionalFilter.get());
    }

    @Test
    public void whenFilterCanNotFindByUserId() {
        var optionalFilter = filterRepository.getByUserId(-1);
        assertTrue(optionalFilter.isEmpty());
    }

    @Test
    public void whenFilterSavedThenDeleteByUserId() {
        var filter = new Filter(1, 1, 1);
        entityManager.persist(filter);
        var optionalFilter = filterRepository.findById(1);
        assertTrue(optionalFilter.isPresent());
        assertThat(filter).isEqualTo(optionalFilter.get());
        filterRepository.deleteByUserId(1);
        optionalFilter = filterRepository.findById(1);
        assertTrue(optionalFilter.isEmpty());
    }

    @Test
    public void whenTryToDeleteFilterByIncorrectUserId() {
        entityManager.createQuery("delete from interview").executeUpdate();
        assertThat(Optional.of(0)).isEqualTo(filterRepository.deleteByUserId(1));
    }
}
