--drop table product;
--create table product(
--    product_id  number(10),
--    pname       varchar(30),
--    quantity    number(10),
--    price       number(10)
--);
----기본키
--alter table product add constraint product_product_id_pk primary key(product_id);
--
----시퀀스생성
--drop sequence product_product_id_seq;
--create sequence product_product_id_seq;
--
--
----생성--
--insert into product(product_id,pname,quantity,price)
--     values(product_product_id_seq.nextval, '컴퓨터', 5, 1000000);
--
--insert into product(product_id,pname,quantity,price)
--     values(product_product_id_seq.nextval, '모니터', 5, 500000);
--
--insert into product(product_id,pname,quantity,price)
--     values(product_product_id_seq.nextval, '프린터', 1, 300000);
--
----조회--
--select product_id, pname, quantity, price
--  from product
-- where product_id = 2;
--
----수정--
--update product
--   set pname = '컴퓨터2',
--       quantity = 10,
--       price = 1200000;
--
----삭제
--delete from product where product_id = 1;
--
----전체조회-
--select product_id,pname,quantity,price from product;
--
--commit;

DROP TABLE member;

DROP TABLE board_file_table;

DROP TABLE comment_table;

DROP TABLE board;

DROP SEQUENCE memberid_seq;

DROP SEQUENCE id_seq;

CREATE SEQUENCE MEMBERID_SEQ;

CREATE SEQUENCE ID_seq;

CREATE TABLE member

(
  memberid NUMBER primary key,
  id       VARCHAR2(20 BYTE),
  pw       VARCHAR2(40 BYTE),
  nick     VARCHAR2(40 BYTE),
  email    VARCHAR2(50 BYTE),
  gender   VARCHAR2(20 BYTE),
  age      VARCHAR2(20 BYTE),
  gubun    VARCHAR2(20 BYTE)
);

CREATE TABLE board
(
  ID              NUMBER primary key,
  BOARD_WRITER    VARCHAR2(20 BYTE),
  BOARD_PASS      VARCHAR2(255 BYTE),
  BOARD_TITLE     VARCHAR2(255 BYTE),
  BOARD_CONTENTS  VARCHAR2(500 BYTE),
  BOARD_HITS      NUMBER(9,0),
  CREATE_TIME     TIMESTAMP(6),
  UPDATE_TIME     TIMESTAMP(6),
  FILEATTACHED    NUMBER(9,0)
);



CREATE TABLE board_file_table
(
  id                  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  create_time         TIMESTAMP(6),
  update_time         TIMESTAMP(6),
  original_filename   VARCHAR2(255),
  stored_filename     VARCHAR2(255),
  board_id            NUMBER,
   CONSTRAINT Board_File_Table_Foreign_Key
    FOREIGN KEY (board_id) REFERENCES board (id) ON DELETE CASCADE
);


CREATE TABLE comment_table
(
  id                  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  create_time         TIMESTAMP(6),
  update_time         TIMESTAMP(6),
  comment_Writer      VARCHAR2(20),
  comment_Contents    VARCHAR2(255),
  board_id            NUMBER,
  CONSTRAINT comment_table_Foreign_Key
    FOREIGN KEY (board_id) REFERENCES board (id) ON DELETE CASCADE
);

COMMIT;

