package ru.checkdev.mock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
