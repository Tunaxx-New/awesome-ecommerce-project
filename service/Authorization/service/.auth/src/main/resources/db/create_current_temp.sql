create table _authorization (id integer not null, email varchar(255), password varchar(255), role enum ('ADMIN','MODERATOR','USER'), primary key (id)) engine=InnoDB;
create table _authorization_seq (next_val bigint) engine=InnoDB;
insert into _authorization_seq values ( 1 );
create table reset_token (user_id integer not null, expiry_date datetime(6), id bigint not null auto_increment, token varchar(255), primary key (id)) engine=InnoDB;
alter table _authorization add constraint UK7y1t448a59bcwe7v5kx0g3o4x unique (id, email);
alter table _authorization add constraint UK_8s36ywgnws4k1ij2n3tj6l2dm unique (email);
alter table reset_token add constraint UK_fbfq7c1c1wxpt21p6jd2jtvhj unique (user_id);
alter table reset_token add constraint UK_shiutqgqq3m7hdrlmckbk4am6 unique (token);
alter table reset_token add constraint FK1vxrrkx9w3pui6hnfi253ey16 foreign key (user_id) references _authorization (id);
