package ru.checkdev.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.Notify;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class NotificationService {

    private final TemplateService templates;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    public NotificationService(final TemplateService templates) {
        this.templates = templates;
    }

    public void put(final Notify notify) {
        this.scheduler.execute(() -> this.templates.send(notify));
    }

    @PreDestroy
    public void close() {
        this.scheduler.shutdown();
    }
}
