package ru.checkdev.auth.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author parsentev
 * @since 21.09.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {
    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository persons;

    @After
    @Test
    public void whenRegDuplicatePersonThenResultEmpty() {
        Profile profile = new Profile("Петр Арсентьев", "parsentev@yandex.ru", "password");
        this.service.reg(profile);
        Optional<Profile> result = this.service.reg(profile);
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void whenRegPersonRolesThenDropRoles() {
        Profile profile = new Profile("Петр Арсентьев", "parsentev@yandex.ru", "password");
        profile.setKey("test");
        this.persons.save(profile);
    }

    @Test
    public void whenSelectAllPersonsThenListContainTestRecord() {
        List<Profile> profileList = this.service.getAll();
        assertTrue(profileList.size() > 0);
    }
}