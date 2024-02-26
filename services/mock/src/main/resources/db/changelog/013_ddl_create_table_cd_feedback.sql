--Создание таблицы для хранения отзывов о собеседовании
CREATE TABLE cd_feedback
(
    id                serial primary key,
    interview_id      int not null references interview(id),
    user_id           int not null,
    role_in_interview int not null,
    text_feedback     text,
    scope             int
);