CREATE DATABASE quarkus-social;

CREATE TABLE USERS (
	id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
)

CREATE TABLE POSTS (
    id bigserial not null primary key,
    post_text varchar(450) not null,
    created_at timestamp not null,
    user_id bigint not null references USERS(id)
)