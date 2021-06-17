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