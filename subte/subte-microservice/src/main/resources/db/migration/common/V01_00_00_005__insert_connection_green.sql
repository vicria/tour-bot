-- -- green line connections
SELECT insert_connection('9 de Julio', 'D🟢', 'Carlos Pellegrini', 'B🔴', null, null, 2.02);
SELECT insert_connection('9 de Julio', 'D🟢', 'Tribunales Teatro Colon', 'D🟢', 'Congreso de Tucuman', 'D🟢', 2.02);
SELECT insert_connection('Tribunales Teatro Colon', 'D🟢', '9 de Julio', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Tribunales Teatro Colon', 'D🟢', 'Callao', 'D🟢', 'Congreso de Tucuman', 'D🟢', 2.02);
SELECT insert_connection('Callao', 'D🟢', 'Tribunales Teatro Colon', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Callao', 'D🟢', 'Facultad de Medicina', 'D🟢', 'Congreso de Tucuman', 'D🟢', 2.02);
SELECT insert_connection('Facultad de Medicina', 'D🟢', 'Callao', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Facultad de Medicina', 'D🟢', 'Pueyrredón', 'D🟢', 'Congreso de Tucuman', 'D🟢', 2.02);
SELECT insert_connection('Pueyrredón', 'D🟢', 'Facultad de Medicina', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Pueyrredón', 'D🟢', 'Santa Fe - Carlos Jáuregui', 'H🟡', null, null, 2.02);