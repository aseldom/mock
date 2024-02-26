package ru.checkdev.desc.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.desc.domain.Topic;
import ru.checkdev.desc.dto.TopicDTO;
import ru.checkdev.desc.repository.TopicRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    public Optional<Topic> findById(int topicId) {
        return topicRepository.findById(topicId);
    }

    public void delete(int categoryId) {
        topicRepository.deleteById(categoryId);
    }

    public Topic create(Topic topic) {
        topic.setCreated(Calendar.getInstance());
        return topicRepository.save(topic);
    }

    public void update(Topic topic) {
        var proxyOpt = topicRepository.findById(topic.getId());
        if (proxyOpt.isPresent()) {
            var proxy = proxyOpt.get();
            proxy.setName(topic.getName());
            proxy.setPosition(topic.getPosition());
            proxy.setText(topic.getText());
            proxy.setUpdated(Calendar.getInstance());
            topicRepository.save(proxy);
        }
    }

    public List<Topic> findByCategory(int id) {
        return new ArrayList<>(topicRepository.findByCategoryIdOrderByPositionAsc(id));
    }

    public List<Topic> getAll() {
        return new ArrayList<>(topicRepository.findAllByOrderByPositionAsc());
    }

    public void incrementTotal(int id) {
        topicRepository.incrementTotal(id);
    }

    public Optional<String> getNameById(int id) {
        return topicRepository.getNameById(id);
    }

    public List<TopicDTO> getTopicDTOsByCategoryId(int categoryId) {
        List<TopicDTO> result = new ArrayList<>();
        var topics = topicRepository.findByCategoryIdOrderByPositionAsc(categoryId);
        for (var topic : topics) {
            result.add(new TopicDTO(topic.getId(), topic.getName()));
        }
        return result;
    }
}
