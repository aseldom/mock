package ru.job4j.site.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * CheckDev пробное собеседование
 * TypeInterviewsTest
 *
 * @author Dmitry Stepanov
 * @version 08.10.2023 01:18
 */
class StatusInterviewTest {
    @Test
    void whenUnknownGetID0GetInfoThenTrue() {
        var unknownId = 0;
        var unknownInfo = "Неизвестен";
        var isUnknown = StatusInterview.IS_UNKNOWN;
        assertThat(isUnknown.getId()).isEqualTo(unknownId);
        assertThat(isUnknown.getInfo()).isEqualTo(unknownInfo);
    }

    @Test
    void whenNewGetID1GetInfoThenTrue() {
        var newId = 1;
        var newInfo = "Новое";
        var isNew = StatusInterview.IS_NEW;
        assertThat(isNew.getId()).isEqualTo(newId);
        assertThat(isNew.getInfo()).isEqualTo(newInfo);
    }

    @Test
    void whenProgressGetID2GetInfoThenTrue() {
        var progressId = 2;
        var progressInfo = "В процессе";
        var inProgress = StatusInterview.IN_PROGRESS;
        assertThat(inProgress.getId()).isEqualTo(progressId);
        assertThat(inProgress.getInfo()).isEqualTo(progressInfo);
    }

    @Test
    void whenFeedbackGetID3GetInfoThenTrue() {
        var progressId = 3;
        var progressInfo = "Ожидает отзыв";
        var inProgress = StatusInterview.IS_FEEDBACK;
        assertThat(inProgress.getId()).isEqualTo(progressId);
        assertThat(inProgress.getInfo()).isEqualTo(progressInfo);
    }

    @Test
    void whenCompletedGetID3GetInfoThenTrue() {
        var completedId = 4;
        var completedInfo = "Завершено";
        var isCompleted = StatusInterview.IS_COMPLETED;
        assertThat(isCompleted.getId()).isEqualTo(completedId);
        assertThat(isCompleted.getInfo()).isEqualTo(completedInfo);
    }

    @Test
    void whenCanceledGetID4GetInfoThenTrue() {
        var canceledId = 5;
        var canceledInfo = "Отменено";
        var isCanceled = StatusInterview.IS_CANCELED;
        assertThat(isCanceled.getId()).isEqualTo(canceledId);
        assertThat(isCanceled.getInfo()).isEqualTo(canceledInfo);
    }
}