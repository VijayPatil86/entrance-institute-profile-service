create table STUDENT_PROFILE(
	PROFILE_ID bigint primary key,
	FIRST_NAME varchar(50) not null,
	LAST_NAME varchar(50) not null,
	DATE_OF_BIRTH date not null,
	GENDER char(1) not null check(GENDER in ('M', 'F')),
	PHONE_NUMBER varchar(10) unique not null,
	ADDRESS_LINE1 text not null,
	ADDRESS_LINE2 text,
	CITY varchar(50) not null,
	STATE varchar(50) not null,
	PIN_CODE varchar(6) not null,
	SCHOOL_NAME text not null,
	BOARD_NAME varchar(100) not null,
	YEAR_OF_PASSING smallint not null,
	PERCENTAGE decimal(5, 2) not null,	-- 100.00
	foreign key (PROFILE_ID) references USER_LOGIN(USER_LOGIN_ID),
	ACTIVE boolean not null default true,
	CREATED_AT timestamp with time zone default current_timestamp not null,
	UPDATED_AT timestamp with time zone default current_timestamp not null
);
----------
