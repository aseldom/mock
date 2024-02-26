package ru.job4j.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InterviewStatistic
 * класс для отображения статистики по каждому интервью.
 * 1. Сколько всего участников - participate.
 * 2. Сколько ожидают собеседования - expect.
 * 3. Сколько уже прошло - passed.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.10.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InterviewStatistic {
    /**
     * Общее количество участников по одному собеседованию.
     * Из модели WisherDTO группировка по полю interviewID
     */
    private int participate;
    /**
     * Количество участников которые ожидают сдачи собеседования.
     * Из модели WisherDTO поле approve = false.
     */
    private int expect;
    /**
     * Количество участников которые прошли собеседование
     * из модели WisherDTO поле approve = true.
     */
    private int passed;
}
