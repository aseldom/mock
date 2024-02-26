package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.CategoryDTO;
import ru.job4j.site.dto.TopicDTO;
import ru.job4j.site.dto.TopicLiteDTO;
import ru.job4j.site.dto.TopicIdNameDTO;

import java.util.Calendar;
import java.util.List;

@Service
public class TopicsService {

    public List<TopicDTO> getByCategory(int id) throws JsonProcessingException {
        var text = new RestAuthCall("http://localhost:9902/topics/" + id).get();
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    public TopicDTO getById(int id) throws JsonProcessingException {
        var text = new RestAuthCall("http://localhost:9902/topic/" + id).get();
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    public TopicDTO create(String token, TopicLiteDTO topicLite) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var topic = new TopicDTO();
        topic.setName(topicLite.getName());
        topic.setPosition(topicLite.getPosition());
        topic.setText(topicLite.getText());
        var category = new CategoryDTO();
        category.setId(topicLite.getCategoryId());
        topic.setCategory(category);
        var out = new RestAuthCall("http://localhost:9902/topic/").post(
                token,
                mapper.writeValueAsString(topic)
        );
        return mapper.readValue(out, TopicDTO.class);
    }

    public void update(String token, TopicDTO topic) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        topic.setUpdated(Calendar.getInstance());
        var json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(topic);
        new RestAuthCall("http://localhost:9902/topic/").update(
                token,
                json
        );
    }

    public void delete(String token, int id) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var topic = new TopicDTO();
        topic.setId(id);
        new RestAuthCall("http://localhost:9902/topic/").delete(
                token,
                mapper.writeValueAsString(topic)
        );
    }

    public String getNameById(int id) {
        return new RestAuthCall(String.format("http://localhost:9902/topic/name/%d", id)).get();
    }

    public List<TopicIdNameDTO> getTopicIdNameDtoByCategory(int categoryId)
            throws JsonProcessingException {
        var text = new
                RestAuthCall(String.format("http://localhost:9902/topics/getByCategoryId/%d",
                categoryId)).get();
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }
}
