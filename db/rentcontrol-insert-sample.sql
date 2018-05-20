\connect rentcontrol rentcontrol localhost 5432

insert into rentcontrol.listing
	(address, city, burrough, neighborhood, state, zipcode, modified_by, modified_ts)
values
	('123 N Street', 'New York', 'Brooklyn', 'Bushwick', 'NY', '11206', 'rc', '1900-01-01 00:00:00.000')
,	('333 W Way', 'New York', 'Brooklyn', 'Bushwick', 'NY', '11207', 'rc', '1900-01-01 00:00:00.000')
;
