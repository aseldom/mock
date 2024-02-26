package ru.checkdev.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.checkdev.auth.domain.Notify;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class Messenger {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final String urlNotify;
    private final String access;

    public Messenger(final @Value("${server.notification}") String urlNotify,
                     final @Value("${access.notification}") String access) {
        this.urlNotify = urlNotify;
        this.access = access;
    }


    public void send(Notify notify) {
        this.scheduler.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new OAuthCall().doPost(
                            null,
                            String.format("%s/template/queue?access=%s", urlNotify, access),
                            new ObjectMapper().writeValueAsString(notify)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @PreDestroy
    public void close() {
        this.scheduler.shutdown();
    }
}
