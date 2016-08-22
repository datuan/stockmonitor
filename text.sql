create table user(
    userid nvarchar(20) not null,
    password nvarchar(20),
    name nvarchar(40),
    primary key(userid)
);

create table stock_symbol(
	symbol nvarchar(10) not null,
	name nvarchar(20),
	primary key(symbol)
);

create table follow(
	userid nvarchar(20) not null,
	symbol nvarchar(10) not null,
	primary key(userid,symbol),
	constraint foreign key (userid) references user(userid),
	constraint foreign key (symbol) references stock_symbol(symbol)
);

create table stocks(
	symbol nvarchar(10) not null,
	last_query bigint not null,
	price decimal (5,2) not null,
	lastTrade nvarchar(20),
	primary key(symbol,last_query),
	constraint foreign key (symbol) references stock_symbol(symbol)
);



insert into user(userid) values('user1');

insert into stock_symbol(symbol) values('AAPL');
insert into stock_symbol(symbol) values('MSFT');
insert into stock_symbol(symbol) values('AMZN');
insert into stock_symbol(symbol) values('GOOG');
insert into stock_symbol(symbol) values('YHOO');

insert into follow(userid,symbol) values('user1','GOOG');
insert into follow(userid,symbol) values('user1','AAPL');