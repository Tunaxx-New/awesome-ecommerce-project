alter table _order_item add commission_percentage integer;
alter table _buyers add commission_percentage integer;
alter table _sellers add commission_percentage integer;

alter table _transparent_policy add value integer;

create table order_item_buyer_transparent_policy (buyer_order_item_id integer not null, buyer_transparent_policy_id integer not null) engine=InnoDB;
create table order_item_seller_transparent_policy (seller_order_item_id integer not null, seller_transparent_policy_id integer not null) engine=InnoDB;

alter table order_item_buyer_transparent_policy add constraint FKt3niwdcci21gn3vcuq1w8fyxu foreign key (buyer_transparent_policy_id) references _transparent_policy (id) on delete cascade;
alter table order_item_buyer_transparent_policy add constraint FKmsx5l89yrohwkjkbhfv83bc4a foreign key (buyer_order_item_id) references _order_item (id);
alter table order_item_seller_transparent_policy add constraint FKdgomemsnveapoymf504jsp6s foreign key (seller_transparent_policy_id) references _transparent_policy (id) on delete cascade;
alter table order_item_seller_transparent_policy add constraint FK34p47cxlbrsl792pwthcwdjvd foreign key (seller_order_item_id) references _order_item (id);

-- Changing authentication transparency table
alter table authentication_transparent_policy drop foreign key FKfo9i6u441yv3577rva5ik1m7e;
alter table authentication_transparent_policy drop column transparent_policy_id;

alter table authentication_transparent_policy add column authentication_transparent_policy_id int;
alter table authentication_transparent_policy add constraint FK9htm9a5n2l8s3tvjh8i7xwrg1 foreign key (authentication_transparent_policy_id) references _transparent_policy (id) on delete cascade;
