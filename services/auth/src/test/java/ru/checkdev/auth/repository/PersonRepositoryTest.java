package ru.checkdev.auth.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.auth.dto.ProfileDTO;

import javax.persistence.EntityManager;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * CheckDev пробное собеседование
 * Тест на класс PersonRepository
 *
 * @author Dmitry Stepanov
 * @version 22.09.2023'T'21:14
 */
@RunWith(SpringRunner.class)
@DataJpaTest()
public class PersonRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;

    @Before
    public void clearTable() {
        entityManager.createQuery("delete from person").executeUpdate();
    }

    @Test
    public void injectedComponentAreNotNull() {
        assertNotNull(entityManager);
        assertNotNull(personRepository);
    }

    @Test
    public void whenFindProfileByIdThenReturnNull() {
        ProfileDTO profileDTO = personRepository.findProfileById(-1);
        assertNull(profileDTO);
    }

    @Test
    public void whenFindProfileOrderByCreatedDescThenReturnEmptyList() {
        var listProfileDTO = personRepository.findProfileOrderByCreatedDesc();
        assertThat(listProfileDTO, is(Collections.emptyList()));
    }
}