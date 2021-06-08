create table user_data_entity
(
    id varchar not null
        constraint member_pk
            primary key auto_increment,
    line_id varchar
    ,grade varchar
    ,class integer
    ,number integer
    ,first_name varchar
    ,last_name varchar
);