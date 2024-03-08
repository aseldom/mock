CREATE TABLE subscribe
(
    id                serial primary key,
    chat_id           bigint unique not null
);