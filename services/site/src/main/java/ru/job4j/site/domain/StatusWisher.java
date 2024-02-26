package ru.job4j.site.domain;

/**
 * CheckDev пробное собеседование
 * StatusWisher enum класс описывает статус участника собеседования.
 * В модели Wisher поле int statusWisher определяет статус участника в собеседовании.
 * Для корректной его обработки используется следующие принятые понятия: РАССМАТРИВАЕТСЯ, СОГЛАСОВАНО, ОТКЛАНЕНО.
 * 0 - Неизвестен IS_UNKNOWN
 * 1 - Рассматривается IS_CONSIDERED
 * 2 - Отклонено IS_REJECTED
 * 3 - Согласовано IS_DISMISSED
 * 4 - Завершено IS_COMPLETED
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 19.10.2023
 */
public enum StatusWisher {
    IS_UNKNOWN(0, "Неизвестен"),
    IS_CONSIDERED(1, "Рассматривается"),
    IS_REJECTED(2, "Отклонено"),
    IS_DISMISSED(3, "Согласовано"),
    IS_COMPLETED(4, "Завершено");

    private final int id;
    private final String info;

    StatusWisher(int id, String info) {
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
