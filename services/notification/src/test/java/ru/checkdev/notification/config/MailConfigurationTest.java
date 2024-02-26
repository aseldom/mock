package ru.checkdev.notification.config;

import org.junit.Ignore;
import org.junit.Test;
import ru.checkdev.notification.domain.Setting;

import java.util.ArrayList;
import java.util.List;

@Ignore
public class MailConfigurationTest {
    @Test
    public void send() throws Exception {
        List<Setting> config = new ArrayList<>();
        config.add(new Setting(Setting.Key.FROM, "support@job4j.ru"));
        config.add(new Setting(Setting.Key.HOST, "smtp.yandex.ru"));
        config.add(new Setting(Setting.Key.AUTH, "true"));
        config.add(new Setting(Setting.Key.PORT, "465"));
        config.add(new Setting(Setting.Key.USERNAME, "support@job4j.ru"));
        config.add(new Setting(Setting.Key.PASSWORD, ""));

        new MailConfiguration().send(
                "Регистрация на Job4j",
                "Добрый день.<br/>"
                        + "Для активации учетной записи перейдите по ссылке <a href='http://job4j.ru/'>Job4j.ru</a><br/>"
                        + "C уважением, Команда Job4j<br/><br/>"
                        + "Это письмо было отправлено на адрес arsentev.pert@gmail.com, "
                        + "так как Вы подписаны на рассылку о сервисе Job4j. <br/>"
                        + "Вы можете отказаться от нее <a href='http://job4j.ru/unsubscribe?notificationId='>отписаться</a>.",
                "arsentev.pert@gmail.com",
                config
        );
    }

}