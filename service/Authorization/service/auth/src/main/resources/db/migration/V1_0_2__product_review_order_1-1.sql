alter table _product_review add order_item_id int not null;
alter table _product_review add constraint UK_9gp2pskg9gyut6rdbgkn7q8eb unique (order_item_id);
alter table _product_review add constraint FK4fbrwn5nghqnwfu21iif0b3no foreign key (order_item_id) references _order_item (id) on delete cascade;
