-- Скрипт добавляет в таблицу wisher поле status. Поле статус описывает статус участника собеседования.
ALTER TABLE wisher ADD COLUMN status INT DEFAULT 1;