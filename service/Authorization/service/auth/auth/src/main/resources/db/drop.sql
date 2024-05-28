alter table _authentication_role drop foreign key FKc5lx61l1hoongqyde2kntvwje;
alter table _authentication_role drop foreign key FK6myisy5bonv91ld4aj11c7ycg;
alter table _authentication_token drop foreign key FKelnersk5fkswnaqge118slbqv;
alter table _badge drop foreign key FK2j8qq3i2hc167ebjp98a4xkpv;
alter table _badge_tag drop foreign key FKr1dtlfpcu0fb8amfldg7852hh;
alter table _buyers drop foreign key FKrmfopl3g7okcpmfu79k4feotn;
alter table _cart drop foreign key FKpwestf5h4eyrbouti5tvl0d5s;
alter table _cart drop foreign key FK7569s7nmyfxn4ntys3jdahdd3;
alter table _cart drop foreign key FKri9lerjcykbcj0dvfiy5jbo2v;
alter table _cart_item drop foreign key FKmkbtxtd5xkr59p1tdf9g3r0br;
alter table _cart_item drop foreign key FKq7a2ocg7i08wmmiqj16qrm2il;
alter table _order drop foreign key FK435eu2rx3g2y30l5rhj8u96xl;
alter table _order drop foreign key FK4f4vvk9r4aydqeg0m3o475xwx;
alter table _order drop foreign key FKodsq6ce8pas8e375kdkm0d2i7;
alter table _order_item drop foreign key FKkm6x6ueuqv4kgtiluwv49003v;
alter table _order_item drop foreign key FK3u4cex6olyf7tauwhqf2qxi0h;
alter table _product drop foreign key FKb5vbbavo9eakjcgfkvq39chwj;
alter table _product_review drop foreign key FK6o4q1i9iy8s3xil26gawm3j0w;
alter table _product_review drop foreign key FK91549px9csqo5wodkd73skjlx;
alter table _sellers drop foreign key FKp2ktngvj9nk4qhgu062hjfje2;
alter table _transparent_policy_seller drop foreign key FKlbu3ahlljh74aver0e7x5v672;
alter table _transparent_policy_seller drop foreign key FKagy17kcphinaky03gg30e8695;
drop table if exists _authentication;
drop table if exists _authentication_role;
drop table if exists _authentication_seq;
drop table if exists _authentication_token;
drop table if exists _authentication_token_seq;
drop table if exists _badge;
drop table if exists _badge_seq;
drop table if exists _badge_tag;
drop table if exists _badge_tag_seq;
drop table if exists _buyers;
drop table if exists _buyers_seq;
drop table if exists _cart;
drop table if exists _cart_item;
drop table if exists _cart_item_seq;
drop table if exists _cart_seq;
drop table if exists _order;
drop table if exists _order_item;
drop table if exists _order_item_seq;
drop table if exists _order_seq;
drop table if exists _payment_methods;
drop table if exists _payment_methods_seq;
drop table if exists _product;
drop table if exists _product_review;
drop table if exists _product_review_seq;
drop table if exists _product_seq;
drop table if exists _roles;
drop table if exists _roles_seq;
drop table if exists _sellers;
drop table if exists _sellers_seq;
drop table if exists _shipping_addresses;
drop table if exists _shipping_addresses_seq;
drop table if exists _transparent_policy;
drop table if exists _transparent_policy_seller;
drop table if exists _transparent_policy_seller_seq;
drop table if exists _transparent_policy_seq;