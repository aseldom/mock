package ru.checkdev.notification.config;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.checkdev.notification.domain.Setting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MailConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void send(String subject, String body, String to, List<Setting> settings) {
        Map<Setting.Key, Setting> keys = settings.stream().collect(
                Collectors.toMap(Setting::getKey, x -> x)
        );
        Configuration configuration = new Configuration()
                .domain("mail.hunt4.pro")
                .apiKey("c01235aad1495736b5ccfa9aa73ccdc0-a4502f89-b32888cf")
                .from("Команда Hunt4.pro", keys.get(Setting.Key.FROM).getValue());
        Response response = Mail.using(configuration)
                .to(to)
                .subject(subject)
                .text(body)
                .build()
                .send();
        logger.debug("send email to {}, {}", to, response.responseMessage());
    }
}
