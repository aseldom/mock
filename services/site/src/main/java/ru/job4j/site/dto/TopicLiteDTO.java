package ru.job4j.site.dto;

import lombok.Data;

@Data
public class TopicLiteDTO {
    private String name;
    private String text;
    private int categoryId;
    private int position;
}
