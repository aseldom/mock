package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.config.TgConfig;
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
public class ForgetAction implements Action {

    private final TelegramUserService telegramUserService;
    private final TgAuthCallWebClint tgAuthCallWebClint;
    private final String urlSiteChangePassword;
    private final String sl = System.lineSeparator();
    private final TgConfig tgConfig = new TgConfig("tg/", 8);

    @Override
    public BotApiMethod<Message> handle(Message message, Map<String, String> bindingBy) {
        String text;
        String chatId = message.getChatId().toString();
        TelegramUser telegramUser = telegramUserService.findByChatId(message.getChatId().intValue());
        if (telegramUser == null) {
            text = String.format("Для Вашего аккаунта регистрация не выполнена. %s/start", sl);
            return send(chatId, text, bindingBy);
        }

        String password = tgConfig.getPassword();
        PersonDTO personDTO = new PersonDTO("", password, true, null, null);
        try {
            tgAuthCallWebClint
                    .doPost(urlSiteChangePassword + telegramUser.getUserId(), personDTO)
                    .block();
        } catch (Exception e) {
            log.error("WebClient doGet error: {}", e.getMessage());
            text = String.format("Сервис не доступен попробуйте позже %s/start", sl);
            return send(chatId, text, bindingBy);
        }
        return send(chatId, "Новый пароль: " + password, bindingBy);
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
