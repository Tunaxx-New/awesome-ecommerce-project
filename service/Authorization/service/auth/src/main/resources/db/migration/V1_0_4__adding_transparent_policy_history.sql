create table _transparent_policy_history (authentication_id integer not null, id integer not null, transparent_policy_id integer not null, created_time datetime(6), primary key (id)) engine=InnoDB;
create table _transparent_policy_history_seq (next_val bigint) engine=InnoDB;
insert into _transparent_policy_history_seq values ( 1 );

alter table _transparent_policy_history add constraint FKc5vx26um0xal5yk0q3lpvv25d foreign key (authentication_id) references _authentication (id) on delete cascade;
alter table _transparent_policy_history add constraint FKa92c4ymxgj81ycyl1hxlnr4mo foreign key (transparent_policy_id) references _transparent_policy (id) on delete cascade;