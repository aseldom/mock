package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTopicDTO {
    private int id;
    private List<Integer> subscribeTopicIds = new ArrayList<>();
}
