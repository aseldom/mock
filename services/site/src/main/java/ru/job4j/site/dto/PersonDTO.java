package ru.job4j.site.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Calendar;

/**
 * CheckDev пробное собеседование
 * PersonDTO DTO модель для обмена данными пользователя.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 18.09.2023
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDTO {
    private int id;
    private String username;
    private String email;
    private String experience;
    private String salary;
    private PhotoDTO photo;
    private String location;
    private Calendar updated;
    private Calendar created;
}
