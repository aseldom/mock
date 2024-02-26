create table if not exists interview (
id serial primary key ,
type_interview int,
submitter_id int,
title varchar(765),
description text,
contact_by varchar(765),
approximate_date varchar(256)
);