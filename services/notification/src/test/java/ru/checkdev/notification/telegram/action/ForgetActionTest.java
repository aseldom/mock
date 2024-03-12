package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class ForgetActionTest {

    private ForgetAction forgetAction;

    private TelegramUserService telegramUserServiceMock;
    private TgAuthCallWebClint tgAuthCallWebClintMock;

    private Message messageMock;

    private HashMap<String, String> bindingBy = new HashMap<>();

    private String sl = System.lineSeparator();
    private String urlSiteChangePassword = "";

    @BeforeEach
    void init() {
        telegramUserServiceMock = mock(TelegramUserService.class);
        tgAuthCallWebClintMock = mock((TgAuthCallWebClint.class));
        messageMock = mock(Message.class);
        forgetAction = new ForgetAction(telegramUserServiceMock, tgAuthCallWebClintMock, urlSiteChangePassword);
    }

    @Test
    public void whenNotRegisteredThenReturnDemandToRegister() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(null);
        String expected = "Для Вашего аккаунта регистрация не выполнена."
                + sl
                + "/start";
        BotApiMethod<Message> res = forgetAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenAuthServiceIsUnavailableThenReturnDemandToWait() {
        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), any(PersonDTO.class))).thenThrow(RuntimeException.class);
        String expected = "Сервис не доступен попробуйте позже" + sl
                + "/start";
        BotApiMethod<Message> res = forgetAction.handle(messageMock, bindingBy);
        assertThat(res.toString()).contains(expected);
    }

    @Test
    public void whenSuccessRequestThenReturnNameAndEmail() {
        ArgumentCaptor<PersonDTO> personDTOArgumentCaptor = ArgumentCaptor.forClass(PersonDTO.class);

        when(telegramUserServiceMock.findByChatId(anyInt())).thenReturn(new TelegramUser());
        when(tgAuthCallWebClintMock.doPost(anyString(), personDTOArgumentCaptor.capture())).thenReturn(Mono.just(new HashMap<>()));
        BotApiMethod<Message> res = forgetAction.handle(messageMock, bindingBy);
        String expected = "Новый пароль: " + personDTOArgumentCaptor.getValue().getPassword();
        assertThat(res.toString()).contains(expected);
    }

}