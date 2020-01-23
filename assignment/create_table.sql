CREATE TABLE public.user(
   username VARCHAR (50) PRIMARY KEY,
   password VARCHAR (50) NOT NULL,
   name VARCHAR (100) NOT NULL,
   surname VARCHAR (100) NOT NULL,
   date_of_birth DATE NOT NULL,
   last_login TIMESTAMP
);

CREATE TABLE public.order(
   username VARCHAR (50) ,
   order_id VARCHAR (50) ,
   order_date_time TIMESTAMP NOT NULL
);