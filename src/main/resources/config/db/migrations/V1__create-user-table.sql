CREATE TABLE T_USERS (
    id bigint(20) not null AUTO_INCREMENT,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(100) not null,
    password varchar(255) not null,
    constraint users_pk primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;

CREATE UNIQUE index idx_email on T_USERS (email);
