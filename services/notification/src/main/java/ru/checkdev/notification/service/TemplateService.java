/**
 *
 */
package ru.checkdev.notification.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.config.MailConfiguration;
import ru.checkdev.notification.domain.Notify;
import ru.checkdev.notification.domain.Template;
import ru.checkdev.notification.repository.SettingRepository;
import ru.checkdev.notification.repository.TemplateRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author olegbelov
 * @since 24.12.2016
 */
@Service
public class TemplateService {

    private final TemplateRepository templates;

    private final SettingRepository settings;

    @Autowired
    public TemplateService(TemplateRepository templates, final SettingRepository settings) {
        this.templates = templates;
        this.settings = settings;
    }


    public List<Template> findAll() {
        return Lists.newArrayList(this.templates.findAll());
    }


    public Template save(Template template) {
        return this.templates.save(template);
    }


    public Template getById(int id) {
        Optional<Template> result = this.templates.findById(id);
        return result.orElseGet(Template::new);
    }

    public Template findById(int id) {
        return this.templates.findById(id).get();
    }

    public boolean delete(int id) {
        var template = new Template();
        template.setId(id);
        this.templates.delete(template);
        return true;
    }

    public Notify send(Notify notify) {
        Template template = this.templates.findByType(notify.getTemplate());
        SimpleGenerator generator = new SimpleGenerator();
        String subject = generator.generate(template.getSubject(), notify.getKeys());
        String body = new SimpleGenerator().generate(template.getBody(), notify.getKeys());
        new MailConfiguration().send(
                subject, body, notify.getEmail(),
                Lists.newArrayList(this.settings.findAll())
        );
        return notify;
    }
}
