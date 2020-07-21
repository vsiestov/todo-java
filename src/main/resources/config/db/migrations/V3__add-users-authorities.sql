CREATE TABLE T_AUTHORITIES (
    id bigint(20) not null auto_increment,
    name varchar(50) not null,
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;

create table T_USER_AUTHORITIES (
    id bigint(20) not null auto_increment,
    user_id bigint(20) not null,
    authority_id bigint(20) not null,
    primary key (id),
    constraint fk_user_authorities_user_id foreign key (user_id) references T_USERS (id) ON DELETE CASCADE,
    constraint fk_user_authorities_authority_id foreign key (authority_id) references T_AUTHORITIES (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = latin1;

insert into T_AUTHORITIES (id, name) values (1, 'ROLE_ADMIN');
insert into T_AUTHORITIES (id, name) values (2, 'ROLE_USER');

insert into T_USER_AUTHORITIES (user_id, authority_id) select id, 2 from T_USERS;
