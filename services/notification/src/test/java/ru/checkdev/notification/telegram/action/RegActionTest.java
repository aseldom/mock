package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class RegActionTest {

    private RegAction regAction;

    private TelegramUserService telegramUserServiceMock;
    private TgAuthCallWebClint tgAuthCallWebClintMock;

    private Message messageMock = mock(Message.class);

    private TgConfig tgConfigMock = mock(TgConfig.class);

    private HashMap<String, String> bindingBy = new HashMap<>();

    private String sl = System.lineSeparator();
    private String urlSiteAuth = "urlSiteAuth";

    @BeforeEach
    void init() {
        telegramUserServiceMock = mock(TelegramUserService.class);
        tgAuthCallWebClintMock = mock((TgAuthCallWebClint.class));
        regAction = new RegAction(telegramUserServiceMock, tgAuthCallWebClintMock, urlSiteAuth);
    }

    @Test
    public void whenUserAlreadyHaveRegisteredThenReturnRejection() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        String expected = "Для Вашего аккаунта регистрация уже выполнена."
                + sl
                + "/start";
        BotApiMethod<Message> res = regAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenNoUserRegisteredThenReturnMessageToEnterEmail() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(null);

        String expected = "Введите email для регистрации:";
        BotApiMethod<Message> res = regAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenEnteredNotCorrectEmailThenReturnAlarmMessage() {
        String email = "emailemail.org";

        when(messageMock.getText()).thenReturn(email);

        String expected = String.format("Email: %s не корректный.%sпопробуйте снова", email, sl);
        BotApiMethod<Message> res = regAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenEnteredCorrectEmailButServerNotAvailableThenReturnAlarmMessage() {
        String email = "email@email.org";

        when(messageMock.getText()).thenReturn(email);
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenThrow(new RuntimeException("RegActionTest"));

        String expected = String.format("Сервис не доступен попробуйте позже%s/start", sl);
        BotApiMethod<Message> res = regAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenServerReturnedErrorObjectThenReturnErrorMessage() {
        String email = "email@email.org";
        HashMap<String, HashMap<String, String>> ret = new HashMap<>();
        ret.put("error", new HashMap<>());

        when(messageMock.getText()).thenReturn(email);
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenReturn(Mono.just(ret));

        String expected = String.format("Ошибка регистрации:");
        BotApiMethod<Message> res = regAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenSuccessfulRegisterUserThenReturnRegistrationMessage() {
        String email = "email@email.org";
        HashMap<String, Object> innerMap = new HashMap<>();
        innerMap.put("id", 1);
        HashMap<String, Map> outerMap = new HashMap<>();
        outerMap.put("person", innerMap);

        when(messageMock.getText()).thenReturn(email);
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenReturn(Mono.just(outerMap));

        String expected = String.format("Вы зарегистрированы: %sЛогин: %s", sl, email);
        BotApiMethod<Message> res = regAction.callback(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }


}