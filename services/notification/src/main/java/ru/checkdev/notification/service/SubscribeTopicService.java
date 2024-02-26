package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.repository.SubscribeTopicRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscribeTopicService {
    private final SubscribeTopicRepository repository;

    public List<SubscribeTopic> findAll() {
        return repository.findAll();
    }

    public SubscribeTopic save(SubscribeTopic subscribeTopic) {
        return repository.save(subscribeTopic);
    }

    public List<Integer> findTopicByUserId(int userId) {
        return repository.findByUserId(userId).stream()
                .map(x -> x.getTopicId())
                .collect(Collectors.toList());
    }

    public SubscribeTopic delete(SubscribeTopic subscribeTopic) {
        SubscribeTopic rsl = repository
                .findByUserIdAndTopicId(subscribeTopic.getUserId(), subscribeTopic.getTopicId());
        repository.delete(rsl);
        return subscribeTopic;
    }
}