\connect rentcontrol rentcontrol localhost 5432

insert into rentcontrol.listing
	(address, city, burrough, neighborhood, state, zipcode, latitude, longitude, modified_by, modified_ts)
values
	('123 N Street', 'New York', 'Brooklyn', 'Bushwick', 'NY', '11206', 40.700453, -73.949255, 'rc', '1900-01-01 00:00:00.000')
,	('333 W Way', 'New York', 'Brooklyn', 'Bushwick', 'NY', '11207', 40.670925, -73.895216, 'rc', '1900-01-01 00:00:00.000')
;
