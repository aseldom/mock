package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class CheckActionTest {

    private CheckAction checkAction;

    private TelegramUserService telegramUserServiceMock;
    private TgAuthCallWebClint tgAuthCallWebClintMock;

    private Message messageMock;

    private HashMap<String, String> bindingBy = new HashMap<>();

    private String sl = System.lineSeparator();
    private String urlSitePerson = "";

    @BeforeEach
    void init() {
        telegramUserServiceMock = mock(TelegramUserService.class);
        tgAuthCallWebClintMock = mock((TgAuthCallWebClint.class));
        messageMock = mock(Message.class);
        checkAction = new CheckAction(telegramUserServiceMock, tgAuthCallWebClintMock, urlSitePerson);
    }

    @Test
    public void whenNotRegisteredThenReturnDemandToRegister() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(null);
        String expected = "Для Вашего аккаунта регистрация не выполнена."
                + sl
                + "/start";
        BotApiMethod<Message> res = checkAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenAuthServiceIsUnavailableThenReturnDemandToWait() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doGetPerson(anyString())).thenThrow(RuntimeException.class);
        String expected = "Сервис не доступен попробуйте позже" + sl
                + "/start";
        BotApiMethod<Message> res = checkAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenSuccessRequestThenReturnNameAndEmail() {
        Map<String, Map<String, String>> outerMap = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("username", "Alex");
        innerMap.put("email", "email@email.org");
        outerMap.put("person", innerMap);
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doGetPerson(anyString())).thenReturn(Mono.just(outerMap));

        String expected = "ФИО: Alex"
                + sl
                + "Email: email@email.org"
                + sl;
        BotApiMethod<Message> res = checkAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

}