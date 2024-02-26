package ru.job4j.site.dto;

import lombok.Data;

import java.util.Calendar;

@Data
public class TopicDTO {
    private int id;
    private String name;
    private String text;
    private Calendar created;
    private Calendar updated;
    private int total;
    private CategoryDTO category;
    private int position;
}
