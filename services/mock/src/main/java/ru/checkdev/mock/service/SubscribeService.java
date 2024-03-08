package ru.checkdev.mock.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.Subscribe;
import ru.checkdev.mock.repository.SubscribeRepository;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    public Subscribe save(Subscribe subscribe) {
        try {
            return subscribeRepository.save(subscribe);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(long chatId) {
        return subscribeRepository.deleteByChatId(chatId);
    }

}
