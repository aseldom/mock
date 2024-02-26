package ru.checkdev.mock.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.dto.WisherDto;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@DataJpaTest()
@RunWith(SpringRunner.class)
class WisherRepositoryTest {
    private Interview interview;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WisherRepository wisherRepository;

    @BeforeEach
    public void clearTable() {
        interview = new Interview();
        interview.setMode(1);
        interview.setSubmitterId(1);
        interview.setTitle("title");
        interview.setAdditional("additional");
        interview.setContactBy("mail@mail");
        interview.setApproximateDate("approximate");
        interview.setCreateDate(new Timestamp(System.currentTimeMillis()));
        interview.setTopicId(1);
        entityManager.createQuery("delete from wisher").executeUpdate();
        entityManager.createQuery("delete from interview").executeUpdate();
        entityManager.persist(interview);
        entityManager.flush();
    }

    @Test
    public void injectedComponentAreNotNull() {
        assertNotNull(entityManager);
        assertNotNull(wisherRepository);
    }

    @Test
    public void whenFindInterviewByIdThenReturnEmpty() {
        Optional<Wisher> wisher = wisherRepository.findById(-1);
        assertThat(wisher, is(Optional.empty()));
    }

    @Test
    public void whenFindAllInterview() {
        var listWisher = wisherRepository.findAll();
        assertThat(listWisher, is(Collections.emptyList()));
    }

    @Test
    public void whenFindWisherByInterviewIdThenReturnListWisherDto() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        entityManager.persist(wisher);
        entityManager.flush();
        var expect = List.of(
                new WisherDto(wisher.getId(), wisher.getInterview().getId(), wisher.getUserId(), wisher.getContactBy(), wisher.isApprove(), wisher.getStatus()));
        var actual = wisherRepository.findWisherDTOByInterviewId(interview.getId());
        assertThat(actual, is(expect));
    }

    @Test
    public void whenFindWisherByInterviewIdThenReturnEmptyList() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        entityManager.persist(wisher);
        entityManager.flush();
        var actual = wisherRepository.findWisherDTOByInterviewId(-1);
        assertThat(actual, is(Collections.emptyList()));
    }

    @Test
    public void whenFindAllWisherDtoThenReturnListWisherDto() {
        var userId = 1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", false, 1);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.flush();
        var expect = List.of(
                new WisherDto(
                        wisher.getId(), wisher.getInterview().getId(),
                        wisher.getUserId(), wisher.getContactBy(),
                        wisher.isApprove(), wisher.getStatus()),
                new WisherDto(
                        wisher1.getId(), wisher1.getInterview().getId(),
                        wisher1.getUserId(), wisher1.getContactBy(),
                        wisher1.isApprove(), wisher.getStatus()));
        var actual = wisherRepository.findAllWiserDto();
        assertThat(actual, is(expect));
    }

    @Test
    public void whenFindAllWisherDtoThenReturnEmptyList() {
        var actual = wisherRepository.findAllWiserDto();
        assertThat(actual, is(Collections.emptyList()));
    }

    @Test
    void whenSetWisherStatusThenReturnWisherNewStatus() {
        var userId = 1;
        var newStatus = 3;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", false, 1);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.clear();
        wisherRepository.setWisherStatus(interview.getId(), wisher.getId(), newStatus);
        var wisherInDB = wisherRepository.findById(wisher.getId());
        assertTrue(wisherInDB.isPresent());
        assertThat(wisherInDB.get().getStatus(), is(newStatus));
        assertTrue(wisherInDB.get().isApprove());
    }

    @Test
    void whenSetNotWisherStatusThenReturnStatusAnyWisher() {
        var userId = 1;
        var anyStatus = 55;
        var notWisherId = -1;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", false, 1);
        var wisher2 = new Wisher(0, interview, userId, "user_Mail2", false, 1);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.persist(wisher2);
        entityManager.clear();
        wisherRepository.setNotWisherStatus(interview.getId(), notWisherId, anyStatus);
        var wisherInDb = wisherRepository.findById(wisher.getId());
        var wisher1InDb = wisherRepository.findById(wisher1.getId());
        var wisher2InDb = wisherRepository.findById(wisher2.getId());
        assertTrue(wisherInDb.isPresent());
        assertTrue(wisher1InDb.isPresent());
        assertTrue(wisher2InDb.isPresent());
        assertThat(wisherInDb.get().getStatus(), is(anyStatus));
        assertThat(wisher1InDb.get().getStatus(), is(anyStatus));
        assertThat(wisher2InDb.get().getStatus(), is(anyStatus));
        assertFalse(wisherInDb.get().isApprove());
        assertFalse(wisher1InDb.get().isApprove());
        assertFalse(wisher2InDb.get().isApprove());
    }

    @Test
    void whenSetWisherStatusThenReturnOldStatus() {
        var userId = 1;
        var wisherId = -1;
        var newStatus = 3;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        entityManager.persist(wisher);
        entityManager.clear();
        wisherRepository.setWisherStatus(interview.getId(), wisherId, newStatus);
        var wisherInDb = wisherRepository.findById(wisher.getId());
        assertTrue(wisherInDb.isPresent());
        assertThat(wisherInDb.get().getStatus(), is(wisher.getStatus()));
        assertFalse(wisherInDb.get().isApprove());
    }

    @Test
    void whenSetNotWisherStatusThenReturnFirstOldStatusSecondNewStatus() {
        var userId = 1;
        var newStatus = 55;
        var wisher = new Wisher(0, interview, userId, "user_Mail", false, 1);
        var wisher1 = new Wisher(0, interview, userId, "user_Mail1", false, 1);
        entityManager.persist(wisher);
        entityManager.persist(wisher1);
        entityManager.clear();
        wisherRepository.setNotWisherStatus(interview.getId(), wisher.getId(), newStatus);
        var wisherFirstInDb = wisherRepository.findById(wisher.getId());
        var wisherSecondInDb = wisherRepository.findById(wisher1.getId());
        assertTrue(wisherFirstInDb.isPresent());
        assertTrue(wisherSecondInDb.isPresent());
        assertThat(wisherFirstInDb.get().getStatus(), is(wisher.getStatus()));
        assertThat(wisherSecondInDb.get().getStatus(), is(newStatus));
        assertFalse(wisherFirstInDb.get().isApprove());
        assertFalse(wisherSecondInDb.get().isApprove());
    }
}