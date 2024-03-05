package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.TelegramUser;
import ru.checkdev.notification.repository.TelegramUserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepository repository;

    public List<TelegramUser> findAll() {
        return repository.findAll();
    }

    public TelegramUser save(TelegramUser telegramUser) {
        return repository.save(telegramUser);
    }

    public TelegramUser findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public TelegramUser findByChatId(int chatId) {
        return repository.findByChatId(chatId);
    }

}