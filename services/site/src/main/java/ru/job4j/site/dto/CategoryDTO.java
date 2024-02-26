package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private int id;
    private String name;
    private int total;
    private int topicsSize;
    private int position;

    public CategoryDTO(int id, String name) {
        this(id, name, 0, 0, 0);
    }
}
