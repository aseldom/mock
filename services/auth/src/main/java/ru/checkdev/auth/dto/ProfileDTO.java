package ru.checkdev.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Calendar;

/**
 * CheckDev пробное собеседование
 * DTO модель описывает профиль пользователя.
 *
 * @author Dmitry Stepanov
 * @version 22.09.2023'T'20:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfileDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private String username;
    private String experience;
    private Integer photoId;
    /**
     * Поле дата обновления профиля.
     */
    private Calendar updated;
    /**
     * Поле дата создания профиля.
     */
    private Calendar created;
}
