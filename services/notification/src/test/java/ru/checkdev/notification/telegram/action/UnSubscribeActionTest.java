package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.exception.ConstraintKeyException;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.telegram.service.TgMockCallWebClint;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UnSubscribeActionTest {

    private UnsubscribeAction unSubscribeAction;

    private TgMockCallWebClint tgMockCallWebClint;;

    private Message messageMock = mock(Message.class);

    private HashMap<String, String> bindingBy = new HashMap<>();

    private String sl = System.lineSeparator();
    private String urlUserSubscribe = "urlUserSubscribe";

    @BeforeEach
    void init() {
        tgMockCallWebClint = mock((TgMockCallWebClint.class));
        unSubscribeAction = new UnsubscribeAction(
                tgMockCallWebClint,
                urlUserSubscribe
        );
    }

    @Test
    public void whenMockServiceIsUnAvailableThenReturnWarningMessage() {
        when(tgMockCallWebClint.doDelete(anyString())).thenThrow(new RuntimeException("RuntimeException - UnSubscribeActionTest"));
        String expected = String.format("Сервис не доступен попробуйте позже %s/start", sl);

        BotApiMethod<Message> res = unSubscribeAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenUnsubscribeOkThenReturnOkMessage() {
        when(tgMockCallWebClint.doDelete(anyString())).thenReturn(Mono.just(1));
        String expected = "Отписка успешно выполнена";

        BotApiMethod<Message> res = unSubscribeAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenItWasNotSubscribeThenReturnNoSubscribe() {
        when(tgMockCallWebClint.doDelete(anyString())).thenReturn(Mono.just(0));
        String expected = "Вы не были подписаны";

        BotApiMethod<Message> res = unSubscribeAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

}