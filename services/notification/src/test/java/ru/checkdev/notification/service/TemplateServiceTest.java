/**
 *
 */
package ru.checkdev.notification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.domain.Template;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author olegbelov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateServiceTest {

    @Autowired
    TemplateService templateService;

    @Test
    public void whenGetAllTemplatesReturnContainsValue() {
        Template template = this.templateService.save(new Template("TestSubject", "TestBody"));
        List<Template> result = this.templateService.findAll();
        assertTrue(result.contains(template));
    }

    @Test
    public void requestByIDReturnCorrectValue() {
        Template template = this.templateService.save(new Template("TestSubjectByID", "TestBodyByID"));
        Template result = this.templateService.getById(template.getId());
        assertEquals(result, template);
    }

    @Test
    public void whenDeleteTemplateItIsNotExist() {
        Template template = this.templateService.save(new Template("TestSubjectForDelete", "TestBodyForDelete"));
        this.templateService.delete(template.getId());
        List<Template> result = this.templateService.findAll();
        assertFalse(result.contains(template));
    }

}
