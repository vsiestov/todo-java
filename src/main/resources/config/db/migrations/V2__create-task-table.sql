CREATE TABLE T_TASKS (
    id bigint(20) not null AUTO_INCREMENT,
    name varchar(255) not null,
    description varchar(255),
    complete boolean default false,
    user_id bigint(20) not null,
    primary key (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES T_USERS (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = latin1;
