CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'yellow', 'Santa Fe Carlos Jáuregui');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'yellow', 'Córdoba');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'yellow', 'Corrientes');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'yellow', 'Facultad de Derecho');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'yellow', 'Hospitales');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', 'Pueyrredón');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', 'Facultad de Medicina');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', 'Callao');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', 'Tribunales Teatro Colon');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', '9 de Julio');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', 'Catedral');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'green', 'Congreso de Tucuman');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Pueyrredón');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Carlos Pellegrini');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Uruguay');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Callao');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Pasteur AMIA');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Juan Manuel de Rosas Villa Urquiza');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'red', 'Leandro N. Alem');

INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'perehod', 'perehod');

--function for connection
CREATE OR REPLACE FUNCTION get_station_id(name_param VARCHAR, line_param VARCHAR)
    RETURNS UUID AS
$$
BEGIN
    RETURN (SELECT id FROM subte_bot.subte_station WHERE name = name_param AND line = line_param);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_connection(name_from VARCHAR, line_from VARCHAR,
                                             name_to VARCHAR, line_to VARCHAR, last_name VARCHAR, last_line VARCHAR,
                                             t_time FLOAT8)
    RETURNS VOID AS
$$
BEGIN
    INSERT INTO subte_bot.subte_connection (id, travel_time, station_from_id, station_to_id, last_station_id)
    VALUES (gen_random_uuid()::text, t_time, (SELECT get_station_id(name_from, line_from)),
            (SELECT get_station_id(name_to, line_to)),
            (SELECT get_station_id(last_name, last_line)));
END;
$$ LANGUAGE plpgsql;