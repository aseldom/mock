package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {

    private int id;

    private int mode;

    private int status;

    private int submitterId;

    private String title;

    private String additional;

    private String contactBy;

    private String approximateDate;

    private String createDate;

    private int topicId;
}
