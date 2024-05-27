/*
CREATE TABLE
  _authentication (
    id INTEGER NOT NULL,
    is_email_confirmed BOOL DEFAULT FALSE,
    registered_time datetime (6),
    email VARCHAR(255),
    password VARCHAR(255),
    PRIMARY key (id)
  ) engine = InnoDB;

CREATE TABLE
  _authentication_role (
    _authentication_id INTEGER NOT NULL,
    roles enum ('ADMIN', 'BUYER', 'MODERATOR', 'SELLER', 'USER')
  ) engine = InnoDB;

CREATE TABLE
  _authentication_seq (next_val BIGINT) engine = InnoDB;

INSERT INTO
  _authentication_seq
VALUES
  (1);

CREATE TABLE
  _authentication_token (
    authentication_id INTEGER NOT NULL,
    expiry_date datetime (6),
    id BIGINT NOT NULL,
    registered_time datetime (6),
    token VARCHAR(255),
    PRIMARY key (id)
  ) engine = InnoDB;

CREATE TABLE
  _authentication_token_seq (next_val BIGINT) engine = InnoDB;

INSERT INTO
  _authentication_token_seq
VALUES
  (1);

CREATE TABLE
  _buyers (
    authentication_id INTEGER NOT NULL,
    birthday DATE,
    id INTEGER NOT NULL,
    registered_time datetime (6),
    bio VARCHAR(255),
    name VARCHAR(255),
    surname VARCHAR(255),
    PRIMARY key (id)
  ) engine = InnoDB;

CREATE TABLE
  _buyers_seq (next_val BIGINT) engine = InnoDB;

INSERT INTO
  _buyers_seq
VALUES
  (1);

ALTER TABLE _authentication add CONSTRAINT UK_q3hi1u99dl0vldawquw302hpa UNIQUE (email);

ALTER TABLE _authentication_token add CONSTRAINT UK_2y2t7lg09logmpsysk8imbo1r UNIQUE (authentication_id);

ALTER TABLE _authentication_token add CONSTRAINT UK_fx6ru820sw08xjufceif5pqgh UNIQUE (token);

ALTER TABLE _buyers add CONSTRAINT UK_51w0up1o848genfgrjyqqngys UNIQUE (authentication_id);

ALTER TABLE _authentication_role add CONSTRAINT FKmc9jpad46p5p6d6mqk9qmwaca FOREIGN key (_authentication_id) REFERENCES _authentication (id);

ALTER TABLE _authentication_token add CONSTRAINT FKelnersk5fkswnaqge118slbqv FOREIGN key (authentication_id) REFERENCES _authentication (id);

ALTER TABLE _buyers add CONSTRAINT FKrmfopl3g7okcpmfu79k4feotn FOREIGN key (authentication_id) REFERENCES _authentication (id);
*/