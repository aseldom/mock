package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class InfoActionTest {

    private List<String> actions = List.of("/start - напечатать список доступных команд",
            "/new - регистрация нового пользователя",
            "/check - выдать ФИО и почту, привязанную к этому аккаунту",
            "/forget - восстановление пароля");

    private InfoAction infoAction = new InfoAction(actions);
    private Message messageMock = mock(Message.class);
    private String sl = System.lineSeparator();

    @Test
    public void whenGetInfoActionThenReturnMenu() {
        BotApiMethod<Message> actual = infoAction.handle(messageMock, new HashMap<>());
        var out = new StringBuilder();
        out.append("Выберите действие:").append(sl);
        for (String action : actions) {
            out.append(action).append(sl);
        }
        String expected = out.toString();
        assertThat(actual.toString()).contains(expected);
    }


}