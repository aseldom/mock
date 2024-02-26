create table if not exists wisher (
id serial primary key ,
interview_id int references interview(id),
user_id int,
contact_by varchar(765),
approve boolean default false
);