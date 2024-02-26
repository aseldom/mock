package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * WisherDTO DTO модель участника собеседования.
 * Используется для обмена с сервисом mock
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 11.10.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WisherDto {

    private int id;

    @NotNull(message = "Title must be non null")
    private int interviewId;

    @NotNull(message = "Title must be non null")
    private int userId;

    @NotBlank(message = "Title must be not empty")
    private String contactBy;

    private boolean approve;
    /**
     * Соответствует id enum StatusWisher.
     */
    private int status;
}
