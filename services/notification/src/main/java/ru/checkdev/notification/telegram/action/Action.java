package ru.checkdev.notification.telegram.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * 3. Мидл
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
public interface Action {
    BotApiMethod<Message> handle(Message message);

    BotApiMethod<Message> callback(Message message);
}
