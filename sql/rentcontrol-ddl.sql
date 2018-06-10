drop schema if exists rentcontrol cascade;
drop database if exists rentcontrol;
drop role if exists rentcontrol;

create role rentcontrol
	with login 
	encrypted password 'rentcontrol';
create database rentcontrol
	with owner rentcontrol;

\connect rentcontrol rentcontrol localhost 5432

create schema rentcontrol;

create table rentcontrol.listing (
	listing_id serial primary key
,	address varchar(255)
,	city varchar(255)
,	burrough varchar(255)
,	neighborhood varchar(255)
,	state varchar(2)
,	zipcode varchar(5) not null
,	latitude numeric(9, 6)
,	longitude numeric(9, 6)
,	modified_by varchar(255)
,	modified_ts timestamp
)
;
