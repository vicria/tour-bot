CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Facultad de Derecho');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Las Heras');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Santa Fe - Carlos Jáuregui');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Córdoba');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Corrientes');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Once - 30 de Diciembre');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Venezuela');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Humberto 1');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Inclán Mezquita Al Ahmad');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Caseros');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Parque Patricios');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'H🟡', 'Hospitales');

INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', 'Pueyrredón');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', 'Facultad de Medicina');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', 'Callao');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', 'Tribunales Teatro Colon');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', '9 de Julio');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', 'Catedral');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'D🟢', 'Congreso de Tucuman');

INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Pueyrredón');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Carlos Pellegrini');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Uruguay');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Callao');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Pasteur AMIA');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Juan Manuel de Rosas Villa Urquiza');
INSERT INTO subte_bot.subte_station (id, line, name) VALUES (gen_random_uuid()::text, 'B🔴', 'Leandro N. Alem');

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