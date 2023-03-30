-- -- green line connections
SELECT insert_connection('9 de Julio', 'green', 'Carlos Pellegrini', 'red', null, null, 2.02);
SELECT insert_connection('9 de Julio', 'green', 'Tribunales Teatro Colon', 'green', 'Congreso de Tucuman', 'green', 2.02);
SELECT insert_connection('Tribunales Teatro Colon', 'green', '9 de Julio', 'green', 'Catedral', 'green', 2.02);
SELECT insert_connection('Tribunales Teatro Colon', 'green', 'Callao', 'green', 'Congreso de Tucuman', 'green', 2.02);
SELECT insert_connection('Callao', 'green', 'Tribunales Teatro Colon', 'green', 'Catedral', 'green', 2.02);
SELECT insert_connection('Callao', 'green', 'Facultad de Medicina', 'green', 'Congreso de Tucuman', 'green', 2.02);
SELECT insert_connection('Facultad de Medicina', 'green', 'Callao', 'green', 'Catedral', 'green', 2.02);
SELECT insert_connection('Facultad de Medicina', 'green', 'Pueyrredón', 'green', 'Congreso de Tucuman', 'green', 2.02);
SELECT insert_connection('Pueyrredón', 'green', 'Facultad de Medicina', 'green', 'Catedral', 'green', 2.02);
SELECT insert_connection('Pueyrredón', 'green', 'Santa Fe - Carlos Jáuregui', 'yellow', null, null, 2.02);