-- yellow line connections
SELECT insert_connection('Facultad de Derecho', 'yellow', 'Las Heras', 'yellow', 'Hospitales', 'yellow', 1.21);
SELECT insert_connection('Las Heras', 'yellow', 'Facultad de Derecho', 'yellow', 'Facultad de Derecho', 'yellow', 1.21);
SELECT insert_connection('Las Heras', 'yellow', 'Santa Fe - Carlos Jáuregui', 'yellow', 'Hospitales', 'yellow', 2.31);
SELECT insert_connection('Santa Fe - Carlos Jáuregui', 'yellow', 'Las Heras', 'yellow', 'Facultad de Derecho', 'yellow', 2.31);
SELECT insert_connection('Santa Fe - Carlos Jáuregui', 'yellow', 'Pueyrredón', 'green', null, null, 5);
SELECT insert_connection('Santa Fe - Carlos Jáuregui', 'yellow', 'Córdoba', 'yellow', 'Hospitales', 'yellow', 1.1);
SELECT insert_connection('Córdoba', 'yellow', 'Santa Fe - Carlos Jáuregui', 'yellow', 'Facultad de Derecho', 'yellow', 1.1);
SELECT insert_connection('Córdoba', 'yellow', 'Corrientes', 'yellow', 'Hospitales', 'yellow', 1.2);
SELECT insert_connection('Corrientes', 'yellow', 'Córdoba', 'yellow', 'Facultad de Derecho', 'yellow', 1.2);
SELECT insert_connection('Corrientes', 'yellow', 'Pueyrredón', 'red', null, null, 5);
SELECT insert_connection('Corrientes', 'yellow', 'Once - 30 de Diciembre', 'yellow', 'Hospitales', 'yellow', 2.00);
SELECT insert_connection('Once - 30 de Diciembre', 'yellow', 'Corrientes', 'yellow', 'Facultad de Derecho', 'yellow', 2.00);
--blue perehod
SELECT insert_connection('Once - 30 de Diciembre', 'yellow', 'Venezuela', 'yellow', 'Hospitales', 'yellow', 3.12);
SELECT insert_connection('Venezuela', 'yellow', 'Once - 30 de Diciembre', 'yellow', 'Facultad de Derecho', 'yellow', 3.12);
SELECT insert_connection('Venezuela', 'yellow', 'Humberto 1', 'yellow', 'Hospitales', 'yellow', 2.02);
SELECT insert_connection('Humberto 1', 'yellow', 'Venezuela', 'yellow', 'Facultad de Derecho', 'yellow', 2.02);
SELECT insert_connection('Humberto 1', 'yellow', 'Inclán Mezquita Al Ahmad', 'yellow', 'Hospitales', 'yellow', 1.1);
SELECT insert_connection('Inclán Mezquita Al Ahmad', 'yellow', 'Humberto 1', 'yellow', 'Facultad de Derecho', 'yellow', 1.1);
SELECT insert_connection('Inclán Mezquita Al Ahmad', 'yellow', 'Caseros', 'yellow', 'Hospitales', 'yellow', 1.32);
SELECT insert_connection('Caseros', 'yellow', 'Inclán Mezquita Al Ahmad', 'yellow', 'Facultad de Derecho', 'yellow', 1.32);
SELECT insert_connection('Caseros', 'yellow', 'Parque Patricios', 'yellow', 'Hospitales', 'yellow', 2.22);
SELECT insert_connection('Parque Patricios', 'yellow', 'Caseros', 'yellow', 'Facultad de Derecho', 'yellow', 2.22);
SELECT insert_connection('Parque Patricios', 'yellow', 'Hospitales', 'yellow', 'Hospitales', 'yellow', 0.85);
SELECT insert_connection('Hospitales', 'yellow', 'Parque Patricios', 'yellow', 'Facultad de Derecho', 'yellow', 0.85);






