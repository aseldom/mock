package ru.job4j.site.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private int id;
    private String name;
    private int total;
    private int position;
}
