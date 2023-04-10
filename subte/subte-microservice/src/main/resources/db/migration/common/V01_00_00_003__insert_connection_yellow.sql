-- yellow line connections
SELECT insert_connection('Facultad de Derecho', 'H🟡', 'Las Heras', 'H🟡', 'Hospitales', 'H🟡', 1.21);
SELECT insert_connection('Las Heras', 'H🟡', 'Facultad de Derecho', 'H🟡', 'Facultad de Derecho', 'H🟡', 1.21);
SELECT insert_connection('Las Heras', 'H🟡', 'Santa Fe - Carlos Jáuregui', 'H🟡', 'Hospitales', 'H🟡', 2.31);
SELECT insert_connection('Santa Fe - Carlos Jáuregui', 'H🟡', 'Las Heras', 'H🟡', 'Facultad de Derecho', 'H🟡', 2.31);
SELECT insert_connection('Santa Fe - Carlos Jáuregui', 'H🟡', 'Pueyrredón', 'D🟢', null, null, 5);
SELECT insert_connection('Santa Fe - Carlos Jáuregui', 'H🟡', 'Córdoba', 'H🟡', 'Hospitales', 'H🟡', 1.1);
SELECT insert_connection('Córdoba', 'H🟡', 'Santa Fe - Carlos Jáuregui', 'H🟡', 'Facultad de Derecho', 'H🟡', 1.1);
SELECT insert_connection('Córdoba', 'H🟡', 'Corrientes', 'H🟡', 'Hospitales', 'H🟡', 1.2);
SELECT insert_connection('Corrientes', 'H🟡', 'Córdoba', 'H🟡', 'Facultad de Derecho', 'H🟡', 1.2);
SELECT insert_connection('Corrientes', 'H🟡', 'Pueyrredón', 'B🔴', null, null, 5);
SELECT insert_connection('Corrientes', 'H🟡', 'Once - 30 de Diciembre', 'H🟡', 'Hospitales', 'H🟡', 2.00);
SELECT insert_connection('Once - 30 de Diciembre', 'H🟡', 'Corrientes', 'H🟡', 'Facultad de Derecho', 'H🟡', 2.00);
--blue perehod
SELECT insert_connection('Once - 30 de Diciembre', 'H🟡', 'Venezuela', 'H🟡', 'Hospitales', 'H🟡', 3.12);
SELECT insert_connection('Venezuela', 'H🟡', 'Once - 30 de Diciembre', 'H🟡', 'Facultad de Derecho', 'H🟡', 3.12);
SELECT insert_connection('Venezuela', 'H🟡', 'Humberto 1', 'H🟡', 'Hospitales', 'H🟡', 2.02);
SELECT insert_connection('Humberto 1', 'H🟡', 'Venezuela', 'H🟡', 'Facultad de Derecho', 'H🟡', 2.02);
SELECT insert_connection('Humberto 1', 'H🟡', 'Inclán Mezquita Al Ahmad', 'H🟡', 'Hospitales', 'H🟡', 1.1);
SELECT insert_connection('Inclán Mezquita Al Ahmad', 'H🟡', 'Humberto 1', 'H🟡', 'Facultad de Derecho', 'H🟡', 1.1);
SELECT insert_connection('Inclán Mezquita Al Ahmad', 'H🟡', 'Caseros', 'H🟡', 'Hospitales', 'H🟡', 1.32);
SELECT insert_connection('Caseros', 'H🟡', 'Inclán Mezquita Al Ahmad', 'H🟡', 'Facultad de Derecho', 'H🟡', 1.32);
SELECT insert_connection('Caseros', 'H🟡', 'Parque Patricios', 'H🟡', 'Hospitales', 'H🟡', 2.22);
SELECT insert_connection('Parque Patricios', 'H🟡', 'Caseros', 'H🟡', 'Facultad de Derecho', 'H🟡', 2.22);
SELECT insert_connection('Parque Patricios', 'H🟡', 'Hospitales', 'H🟡', 'Hospitales', 'H🟡', 0.85);
SELECT insert_connection('Hospitales', 'H🟡', 'Parque Patricios', 'H🟡', 'Facultad de Derecho', 'H🟡', 0.85);






