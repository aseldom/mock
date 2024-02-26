package ru.job4j.site.domain;

/**
 * CheckDev пробное собеседование
 * StatusInterviews enum класс описывает статусы интервью.
 * В модели Interviews поле int status характеризует статус интервью.
 * Для корректной его обработки используется следующие принятые понятия.
 * Если собеседование новое, то есть у него нет одобренного участника, то статус новое.
 * Если автор одобрил участника, то статус в процессе.
 * Если автор или участник оставил отзыв, но не оба, то статут ожидает отзыв.
 * Если автор и участник оставили отзывы, то статус завершено.
 * Если автор отметил собеседование, то статус отменено.
 * 0 - Неизвестен IS_UNKNOWN.
 * 1 - Новое IS_NEW.
 * 2 - В процессе IN_PROGRESS.
 * 3 - Завершено IS_COMPLETED.
 * 4 - Отменено IS_CANCELED.
 *
 * @author Dmitry Stepanov
 * @version 08.10.2023 00:59
 */
public enum StatusInterview {
    IS_UNKNOWN(0, "Неизвестен"),

    IS_NEW(1, "Новое"),

    IN_PROGRESS(2, "В процессе"),

    IS_FEEDBACK(3, "Ожидает отзыв"),

    IS_COMPLETED(4, "Завершено"),

    IS_CANCELED(5, "Отменено");

    private final int id;
    private final String info;

    StatusInterview(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }
}
