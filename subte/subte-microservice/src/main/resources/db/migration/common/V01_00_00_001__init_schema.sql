-- Таблица станций
create table if not exists subte_bot.subte_station
(
	id varchar(255) not null
		constraint subte_station_pkey
			primary key,
	line varchar(20) not null,
	name varchar(50) not null
);
alter table subte_bot.subte_station
add constraint uq_subte_station_line_name unique(line, name);

-- Связи между станциями
create table if not exists subte_bot.subte_connection
(
	id varchar(36) not null
		constraint subte_connection_pkey
			primary key,
	travel_time float8 not null,
	last_station_id varchar(255) not null
		constraint fk_last_station_id_station_id
			references subte_bot.subte_station,
	station_from_id varchar(255) not null
		constraint fk_station_from_id_station_id
			references subte_bot.subte_station,
	station_to_id varchar(255) not null
		constraint fk_station_to_id_station_id
			references subte_bot.subte_station
);

alter table subte_bot.subte_connection
add constraint uq_station_from_id_station_to_id unique(station_from_id, station_to_id);
