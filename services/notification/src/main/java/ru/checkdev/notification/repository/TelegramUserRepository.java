package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.checkdev.notification.domain.TelegramUser;

import java.util.List;

@Repository
public interface TelegramUserRepository extends CrudRepository<TelegramUser, Integer> {
    List<TelegramUser> findAll();

    TelegramUser findByUserId(int userId);

    TelegramUser findByChatId(int chatId);

}