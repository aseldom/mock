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

class SubscribeActionTest {

    private SubscribeAction subscribeAction;

    private TelegramUserService telegramUserServiceMock;
    private TgAuthCallWebClint tgAuthCallWebClintMock;
    private TgMockCallWebClint tgMockCallWebClint;;

    private Message messageMock = mock(Message.class);

    private TgConfig tgConfigMock = mock(TgConfig.class);

    private HashMap<String, String> bindingBy = new HashMap<>();

    private String sl = System.lineSeparator();
    private String urlUserCheck = "urlUserCheck";
    private String urlUserSubscribe = "urlUserSubscribe";

    @BeforeEach
    void init() {
        telegramUserServiceMock = mock(TelegramUserService.class);
        tgAuthCallWebClintMock = mock((TgAuthCallWebClint.class));
        tgMockCallWebClint = mock((TgMockCallWebClint.class));
        subscribeAction = new SubscribeAction(
                telegramUserServiceMock,
                tgAuthCallWebClintMock,
                tgMockCallWebClint,
                urlUserCheck,
                urlUserSubscribe
        );
    }

    @Test
    public void whenUserAlreadyHaveRegisteredThenReturnRejection() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(null);
        String expected = "Для Вашего аккаунта регистрация не выполнена."
                + sl
                + "/new";
        BotApiMethod<Message> res = subscribeAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenNoUserRegisteredThenReturnMessageToEnterEmail() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        String expected = "Введите email:";
        BotApiMethod<Message> res = subscribeAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenEmailOrPasswordAreWrongThenReturnWarningMessage() {
        long chatId = 1L;
        String email = "email.email.org";
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(email);

        String expected = "Введите password:";
        BotApiMethod<Message> res = subscribeAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);

    }

    @Test
    public void whenEmailAlreadyEnteredThenReturnMessageToEnterPassword() {
        long chatId = 1L;
        String email = "email.email.org";
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenReturn(Mono.just(false));
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(email);

        call(String.format("Введены неверные данные %s/start", sl));

    }

    @Test
    public void whenAuthServiceisUnavalableThenReturnWarningMessage() {
        long chatId = 1L;
        String email = "email.email.org";
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenThrow(new RuntimeException("SubscribeActionTest"));
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(email);

        call(String.format("Сервис недоступен попробуйте позже %s/start", sl));

    }

    @Test
    public void whenWasAlreadySubscribedThenReturnWarningMessage() {
        long chatId = 1L;
        String email = "email.email.org";
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenReturn(Mono.just(true));
        when(tgMockCallWebClint.doPost(anyString(), anyLong())).thenThrow(new ConstraintKeyException("ConstraintKeyException - SubscribeActionTest"));
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(email);

        call(String.format("Вы ранее уже были подписаны %s/start", sl));

    }

    @Test
    public void whenMockServiceIsUnavailableThenReturnWarningMessage() {
        long chatId = 1L;
        String email = "email.email.org";
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenReturn(Mono.just(true));
        when(tgMockCallWebClint.doPost(anyString(), anyLong())).thenThrow(new RuntimeException("Exception - SubscribeActionTest"));
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(email);

        call(String.format("Сервис недоступен попробуйте позже %s/start", sl));

    }

    @Test
    public void whenSubscribeThenReturnOkMessage() {
        long chatId = 1L;
        String email = "email.email.org";
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenReturn(Mono.just(true));
        when(tgMockCallWebClint.doPost(anyString(), anyLong())).thenReturn(Mono.just(1));
        when(messageMock.getChatId()).thenReturn(chatId);
        when(messageMock.getText()).thenReturn(email);

        call("Вы подписаны");

    }

    private void call(String message) {
        String expected = "Введите password:";
        BotApiMethod<Message> res = subscribeAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);

        expected = message;
        res = subscribeAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }
}