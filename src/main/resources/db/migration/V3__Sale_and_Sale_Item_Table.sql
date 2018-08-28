create table sale (
  id varchar(36) primary key,
  create_date timestamp not null,
  total decimal(18, 2) not null
);

create table sale_item (
  id varchar(36) primary key,
  product_code varchar (255) not null,
  product_name varchar (255) not null,
  quantity int not null,
  price decimal (18,2) not null,
  sub_total decimal (18,2) not null,

  sale_id varchar (36) not null
);