package ru.checkdev.notification.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.checkdev.notification.service.TelegramUserService;
import ru.checkdev.notification.telegram.action.Action;
import ru.checkdev.notification.telegram.action.CheckAction;
import ru.checkdev.notification.telegram.action.InfoAction;
import ru.checkdev.notification.telegram.action.RegAction;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.List;
import java.util.Map;

/**
 * 3. Мидл
 * Инициализация телеграм бот,
 * username = берем из properties
 * token = берем из properties
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TgRun {

    private final TgAuthCallWebClint tgAuthCallWebClint;
    private final TelegramUserService telegramUserService;
    @Value("${tg.username}")
    private String username;
    @Value("${tg.token}")
    private String token;
    @Value("${server.site.url.login}")
    private String urlSiteAuth;
    @Value("${server.site.url.person}")
    private String urlSitePerson;

    @Bean
    public void initTg() {
        Map<String, Action> actionMap = Map.of(
                "/start", new InfoAction(List.of(
                        "/start - напечатать список доступных команд",
                        "/new - регистрация нового пользователя",
                        "/check - выдать ФИО и почту, привязанную к этому аккаунту",
                        "/forget - восстановление пароля",
                        "/subscribe - подписка, через ввод логина и пароля",
                        "/unsubscribe - отписка, через проверку в chatId")),
                "/new", new RegAction(telegramUserService, tgAuthCallWebClint, urlSiteAuth),
                "/check", new CheckAction(telegramUserService, tgAuthCallWebClint, urlSitePerson)
//                "/forget", new ForgetAction(tgAuthCallWebClint, urlSiteAuth),
//                "/subscribe", new SubscribeAction(tgAuthCallWebClint, urlSiteAuth),
//                "/unsubscribe", new UnsubscribeAction(tgAuthCallWebClint, urlSiteAuth)
        );
        try {
            BotMenu menu = new BotMenu(actionMap, username, token);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(menu);
        } catch (TelegramApiException e) {
            log.error("Telegram bot: {}, ERROR {}", username, e.getMessage());
        }
    }
}
