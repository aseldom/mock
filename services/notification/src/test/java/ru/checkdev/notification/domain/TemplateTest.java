/**
 *
 */
package ru.checkdev.notification.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author olegbelov
 * @since 20.12.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateTest {

    @Test
    public void whenDefaultCounstructorNotNull() {
        Template template = new Template();
        assertNotNull(template);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        Template template = new Template("TestSubject", "TestBody");
        assertNotNull(template);
    }

    @Test
    public void whenIDSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setId(1);
        assertThat(1, is(template.getId()));
    }


    @Test
    public void whenSubjectTypeSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setSubject("NewSubject");
        assertThat("NewSubject", is(template.getSubject()));
    }

    @Test
    public void whenBodyTypeSetandGetEquals() {
        Template template = new Template("TestSubject", "TestBody");
        template.setBody("NewBody");
        assertThat("NewBody", is(template.getBody()));
    }
}
