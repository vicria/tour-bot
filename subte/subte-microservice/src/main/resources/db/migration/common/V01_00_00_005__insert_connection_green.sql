-- -- green line connections
SELECT insert_connection('Catedral', 'D🟢', '9 de Julio', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('9 de Julio', 'D🟢', 'Catedral', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('9 de Julio', 'D🟢', 'Carlos Pellegrini', 'B🔴', null, null, 2.02);
SELECT insert_connection('9 de Julio', 'D🟢', 'Tribunales Teatro Colon', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Tribunales Teatro Colon', 'D🟢', '9 de Julio', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Tribunales Teatro Colon', 'D🟢', 'Callao', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Callao', 'D🟢', 'Tribunales Teatro Colon', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Callao', 'D🟢', 'Facultad de Medicina', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Facultad de Medicina', 'D🟢', 'Callao', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Facultad de Medicina', 'D🟢', 'Pueyrredón', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Pueyrredón', 'D🟢', 'Facultad de Medicina', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Pueyrredón', 'D🟢', 'Santa Fe - Carlos Jáuregui', 'H🟡', null, null, 2.02);


SELECT insert_connection('Pueyrredón', 'D🟢', 'Agüero', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Agüero', 'D🟢', 'Pueyrredón', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Agüero', 'D🟢', 'Bulnes', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Bulnes', 'D🟢', 'Agüero', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Bulnes', 'D🟢', 'Scalabrini Ortiz', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Scalabrini Ortiz', 'D🟢', 'Bulnes', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Scalabrini Ortiz', 'D🟢', 'Plaza Italia', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Plaza Italia', 'D🟢', 'Scalabrini Ortiz', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Plaza Italia', 'D🟢', 'Palermo', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Palermo', 'D🟢', 'Plaza Italia', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Palermo', 'D🟢', 'Ministro Carranza', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Ministro Carranza', 'D🟢', 'Palermo', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Ministro Carranza', 'D🟢', 'Olleros', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Olleros', 'D🟢', 'Ministro Carranza', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Olleros', 'D🟢', 'José Hernández', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('José Hernández', 'D🟢','Olleros', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('José Hernández', 'D🟢', 'Juramento', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);
SELECT insert_connection('Juramento', 'D🟢', 'José Hernández', 'D🟢', 'Catedral', 'D🟢', 2.02);

SELECT insert_connection('Congreso de Tucumán', 'D🟢', 'Juramento', 'D🟢', 'Catedral', 'D🟢', 2.02);
SELECT insert_connection('Juramento', 'D🟢', 'Congreso de Tucumán', 'D🟢', 'Congreso de Tucumán', 'D🟢', 2.02);