create table if not exists user_data
(
    id serial not null
    ,line_id text not null
    ,grade text not null
    ,class integer
    ,number integer
    ,first_name text
    ,last_name text
    ,primary key (id)
);

create table if not exists submission_data
(
    submission_id serial not null
    ,user_id int not null
    ,timestamp timestamp not null
    ,query text
    ,command_type text
    ,states text
    ,primary key (submission_id)
)