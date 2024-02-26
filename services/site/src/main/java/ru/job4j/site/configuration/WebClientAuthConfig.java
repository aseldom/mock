package ru.job4j.site.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * CheckDev пробное собеседование
 * Создание DI объекта WebClient для сервиса auth
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 19.09.2023
 */
@Configuration
public class WebClientAuthConfig {
    /**
     * Из app.properties получаем корневой адрес сервиса AUTH
     */
    @Value("${server.auth}")
    private String urlAuth;

    @Bean
    public WebClient createWebClientAuth() {
        return WebClient.create(urlAuth);
    }
}
