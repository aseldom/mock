package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.SubscribeTopic;
import java.util.List;

public interface SubscribeTopicRepository extends CrudRepository<SubscribeTopic, Integer> {
    @Override
    List<SubscribeTopic> findAll();

    List<SubscribeTopic> findByUserId(int id);

    SubscribeTopic findByUserIdAndTopicId(int userId, int topicId);
}