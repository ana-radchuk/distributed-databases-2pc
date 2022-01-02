------- fly_booking database ------
drop database if exists fly_booking;
create database fly_booking;
\connect fly_booking;

create table public.fly_booking (
    booking_id serial primary key,
    client_name varchar,
    fly_number varchar(8),
    from_airport varchar(3),
    to_airport varchar(3),
    departure_date date
);
------ hotel_booking database -------
drop database if exists hotel_booking;
create database hotel_booking;
\connect hotel_booking;

create table public.hotel_booking(
    booking_id serial primary key,
    client_name varchar,
    hotel_name varchar,
    arrival_date date,
    departure_date date
);

------ account database -------
drop database if exists account;
create database account;
\connect account;

create table public.account(
    account_id serial primary key,
    client_name varchar,
    amount numeric CHECK (amount > 0)
);

insert into public.account values(1, 'Nik', 200);