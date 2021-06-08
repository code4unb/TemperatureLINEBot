create table member
(
    id varchar not null
        constraint member_pk
            primary key auto_increment,
    line_id varchar not null
    ,grade varchar not null
    ,class integer not null
    ,number integer not null
    ,first_name varchar
    ,last_name varchar
);