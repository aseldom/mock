package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeTopicDTO {
    private int userId;
    private int topicId;
}
