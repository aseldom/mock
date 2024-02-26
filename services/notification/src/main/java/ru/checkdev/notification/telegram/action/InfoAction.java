package ru.checkdev.notification.telegram.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

/**
 * 3. Мидл
 * Класс реализует вывод доступных команд телеграмм бота
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
public class InfoAction implements Action {
    private final List<String> actions;

    public InfoAction(List<String> actions) {
        this.actions = actions;
    }

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId().toString();
        String sl = System.lineSeparator();
        var out = new StringBuilder();
        out.append("Выберите действие:").append(sl);
        for (String action : actions) {
            out.append(action).append(sl);
        }
        return new SendMessage(chatId, out.toString());
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}
