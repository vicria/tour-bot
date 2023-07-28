SELECT insert_connection('Retiro', 'C🔵', 'General San Martín', 'C🔵', 'Constitución', 'C🔵', 3);
SELECT insert_connection('Retiro', 'C🔵', 'Retiro', 'E🟣', null, null, 3);

SELECT insert_connection('General San Martín', 'C🔵', 'Retiro', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('General San Martín', 'C🔵', 'Lavalle', 'C🔵', 'Constitución', 'C🔵', 3);

SELECT insert_connection('Lavalle', 'C🔵', 'General San Martín', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('Lavalle', 'C🔵', 'Diagonal Norte', 'C🔵', 'Constitución', 'C🔵', 3);

SELECT insert_connection('Diagonal Norte', 'C🔵', 'Lavalle', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('Diagonal Norte', 'C🔵', 'Avenida de Mayo', 'C🔵', 'Constitución', 'C🔵', 3);
SELECT insert_connection('Diagonal Norte', 'C🔵', '9 de Julio', 'D🟢', null, null, 3);
SELECT insert_connection('Diagonal Norte', 'C🔵', 'Carlos Pellegrini', 'B🔴', null, null, 3);

SELECT insert_connection('Avenida de Mayo', 'C🔵', 'Diagonal Norte', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('Avenida de Mayo', 'C🔵', 'Moreno', 'C🔵', 'Constitución', 'C🔵', 3);
SELECT insert_connection('Avenida de Mayo', 'C🔵', 'Lima', 'A🔵', null, null, 3);

SELECT insert_connection('Moreno', 'C🔵', 'Avenida de Mayo', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('Moreno', 'C🔵', 'Independencia', 'C🔵', 'Constitución', 'C🔵', 3);

SELECT insert_connection('Independencia', 'C🔵', 'Moreno', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('Independencia', 'C🔵', 'San Juan', 'C🔵', 'Constitución', 'C🔵', 3);
SELECT insert_connection('Independencia', 'C🔵', 'Independencia', 'E🟣', null, null, 3);

SELECT insert_connection('San Juan', 'C🔵', 'Independencia', 'C🔵', 'Retiro', 'C🔵', 3);
SELECT insert_connection('San Juan', 'C🔵', 'Constitución', 'C🔵', 'Constitución', 'C🔵', 3);

SELECT insert_connection('Constitución', 'C🔵', 'San Juan', 'C🔵', 'Retiro', 'C🔵', 3);
