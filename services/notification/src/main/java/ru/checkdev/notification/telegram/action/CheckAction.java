package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.Map;

/**
 * 3. Мидл
 * Класс реализует вывод доступных команд телеграмм бота
 *
 * @author Alexaner
 * @since 03.03.2024
 */
@AllArgsConstructor
@Slf4j
public class CheckAction implements Action {

    private final TelegramUserService telegramUserService;
    private final TgAuthCallWebClint tgAuthCallWebClint;
    private final String urlSitePerson;
    private final String sl = System.lineSeparator();

    @Override
    public BotApiMethod<Message> handle(Message message, Map<String, String> bindingBy) {
        String text;
        String chatId = message.getChatId().toString();
        TelegramUser telegramUser = telegramUserService.findByChatId(message.getChatId().intValue());
        if (telegramUser == null) {
            text = "Для Вашего аккаунта регистрация не выполнена."
                    + sl
                    + "/start";
            return send(chatId, text, bindingBy);
        }

        Map<String, Map> result;
        String urlPerson = urlSitePerson + telegramUser.getUserId();
        try {
            result = tgAuthCallWebClint.doGetPerson(urlPerson).block();
        } catch (Exception e) {
            log.error("WebClient doGet error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже" + sl
                    + "/start";
            return send(chatId, text, bindingBy);
        }
        Map person = (Map) result.get("person");

        var out = new StringBuilder();
        out.append("ФИО: ").append(person.get("username")).append(sl);
        out.append("Email: ").append(person.get("email")).append(sl);
        return send(chatId, out.toString(), bindingBy);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return null;
    }

    private BotApiMethod<Message> send(String chatId, String out, Map<String, String> bindingBy) {
        bindingBy.remove(chatId);
        return new SendMessage(chatId, out);
    }
}
