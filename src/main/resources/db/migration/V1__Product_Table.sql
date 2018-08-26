create table product (
  id varchar(32) primary key,
  code varchar (255) not null,
  name varchar (255) not null,
  quantity int not null,
  price decimal (18,2) not null
)