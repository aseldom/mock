package ru.checkdev.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import ru.checkdev.auth.domain.Notify;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class OAuthCallTest {
    @Ignore
    @Test
    public void call() throws Exception {
        new OAuthCall().doPost(
                null,
                String.format("%s/template/queue?access=%s", "https://localhost:9920", "96GcWB8a"),
                new ObjectMapper().writeValueAsString(new Notify("parsentev@yandex.ru", null, null))
        );
    }
}