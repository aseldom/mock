package ru.checkdev.desc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Calendar;

@Entity(name = "cd_topic")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String text;
    private Calendar created;
    private Calendar updated;
    private int total;
    private int position;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
}
