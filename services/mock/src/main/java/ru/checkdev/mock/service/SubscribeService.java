package ru.checkdev.mock.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.Subscribe;
import ru.checkdev.mock.repository.SubscribeRepository;

@Service
@Slf4j
@AllArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    public Subscribe save(Subscribe subscribe) {
        return subscribeRepository.save(subscribe);
    }

    public int deleteByChatId(long chatId) {
        return subscribeRepository.deleteByChatId(chatId);
    }

}
