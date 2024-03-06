package ru.checkdev.notification.telegram.action;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

/**
 * 3. Мидл
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
@Component
public interface Action {
    BotApiMethod<Message> handle(Message message, Map<String, String> bindingBy);

    BotApiMethod<Message> callback(Message message);
}
