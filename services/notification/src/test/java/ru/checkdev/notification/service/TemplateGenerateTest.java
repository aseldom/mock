package ru.checkdev.notification.service;

import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TemplateGenerateTest {
    @Test
    public void whenTemplateHasLoopThenRenderTable() throws IOException, TemplateException {
        SimpleGenerator generator = new SimpleGenerator();
        Map<String, Object> model = new HashMap<>();
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList("name", "email"));
        data.add(Arrays.asList("name", "email"));
        model.put("interviews", data);
        String result = generator.generate(
                "<#list interviews as interview>"
                        + "<#list interview as value>${value} </#list>"
                        + "</#list>", model
        );

        assertThat(result, is("name email name email "));
    }

    @Test
    public void whenHashtable() {
        SimpleGenerator generator = new SimpleGenerator();
        Map<String, Object> model = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("name", "email");
        model.put("interviews", data);
        String result = generator.generate(
                "<#list interviews?keys as key>"
                        + "${interviews[key]}"
                        + "</#list>", model
        );

        assertThat(result, is("email"));
    }
}
