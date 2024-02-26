package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 20.10.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WisherDetailDTO {
    private int id;

    private int interviewId;

    private int userId;

    private String username;

    private String contactBy;

    private boolean approve;
    /**
     * Соответствует id enum StatusWisher.
     */
    private int statusId;

    private String statusName;
}
