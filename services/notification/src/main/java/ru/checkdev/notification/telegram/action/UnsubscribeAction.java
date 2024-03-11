package ru.checkdev.notification.telegram.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.service.TgMockCallWebClint;

import java.util.Map;

/**
 * 3. Мидл
 * Класс реализует отписку пользователя через chatId
 *
 * @author Alexaner
 * @since 03.03.2024
 */
@RequiredArgsConstructor
@Slf4j
public class UnsubscribeAction implements Action {

    private final String sl = System.lineSeparator();
    private final TgMockCallWebClint tgMockCallWebClient;
    private final String urlUserSubscribe;

    @Override
    public BotApiMethod<Message> handle(Message message, Map<String, String> bindingBy) {
        String text;
        String chatId = message.getChatId().toString();
        try {
            int res = (int) tgMockCallWebClient.doDelete(urlUserSubscribe + "/" + message.getChatId()).block();
            if (res != 0) {
                text = "Отписка успешно выполнена";
                return send(chatId, text, bindingBy);
            }
        } catch (Exception e) {
            log.error("WebClient doGet error: {}", e.getMessage());
            text = String.format("Сервис не доступен попробуйте позже %s/start", sl);
            return send(chatId, text, bindingBy);
        }
        text = "Вы не были подписаны";
        return send(chatId, text, bindingBy);
    }

    @Override
    public BotApiMethod<Message> callback(Message message, Map<String, String> bindingBy) {
        return null;
    }

    private BotApiMethod<Message> send(String chatId, String out, Map<String, String> bindingBy) {
        bindingBy.remove(chatId);
        return new SendMessage(chatId, out);
    }
}
