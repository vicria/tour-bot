-- Между станциями Juan Manuel de Rosas Villa Urquiza, Echeverría
SELECT insert_connection('Juan Manuel de Rosas Villa Urquiza', 'B🔴', 'Echeverría', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Echeverría', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);

-- Между станциями Echeverría, De los Incas Parque Chas
SELECT insert_connection('Echeverría', 'B🔴', 'De los Incas Parque Chas', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('De los Incas Parque Chas', 'B🔴', 'Echeverría', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);

-- Между станциями De los Incas Parque Chas, Tronador Villa Ortúzar
SELECT insert_connection('De los Incas Parque Chas', 'B🔴', 'Tronador Villa Ortúzar', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Tronador Villa Ortúzar', 'B🔴', 'De los Incas Parque Chas', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);

-- Между станциями Tronador Villa Ortúzar, F.Lacroze
SELECT insert_connection('Tronador Villa Ortúzar', 'B🔴', 'F.Lacroze', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('F.Lacroze', 'B🔴', 'Tronador Villa Ortúzar', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);

-- Между станциями Dorrego, F.Lacroze
SELECT insert_connection('Dorrego', 'B🔴', 'F.Lacroze', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);
SELECT insert_connection('F.Lacroze', 'B🔴', 'Dorrego', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);

SELECT insert_connection('Malabia O. Publiese', 'B🔴', 'Dorrego', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);
SELECT insert_connection('Dorrego', 'B🔴', 'Malabia O. Publiese', 'B🔴','Leandro N. Alem', 'B🔴', 2.47);

SELECT insert_connection('Malabia O. Publiese', 'B🔴', 'Ángel Gallardo', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Ángel Gallardo', 'B🔴', 'Malabia O. Publiese', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);

SELECT insert_connection('Ángel Gallardo', 'B🔴', 'Medrano Almagro', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Medrano Almagro', 'B🔴', 'Ángel Gallardo', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);
SELECT insert_connection('Medrano Almagro', 'B🔴', 'Carlos Gardel', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Carlos Gardel', 'B🔴', 'Medrano Almagro', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);
SELECT insert_connection('Carlos Gardel', 'B🔴', 'Pueyrredón', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Pueyrredón', 'B🔴', 'Carlos Gardel', 'B🔴',  'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.47);
SELECT insert_connection('Pueyrredón', 'B🔴', 'Corrientes', 'H🟡',  null, null, 2.47);
SELECT insert_connection('Pueyrredón', 'B🔴', 'Pasteur AMIA', 'B🔴',  'Leandro N. Alem', 'B🔴', 2.47);
SELECT insert_connection('Pasteur AMIA', 'B🔴',  'Pueyrredón', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.02);
SELECT insert_connection('Pasteur AMIA', 'B🔴',  'Callao', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.02);
SELECT insert_connection('Callao', 'B🔴', 'Pasteur AMIA', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.02);
SELECT insert_connection('Callao', 'B🔴', 'Uruguay', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.02);
SELECT insert_connection('Uruguay', 'B🔴', 'Callao', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.02);
SELECT insert_connection('Uruguay', 'B🔴', 'Carlos Pellegrini', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.02);
SELECT insert_connection('Carlos Pellegrini', 'B🔴', 'Uruguay', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.02);
SELECT insert_connection('Carlos Pellegrini', 'B🔴', '9 de Julio', 'D🟢', null, null, 2.02);

SELECT insert_connection('Carlos Pellegrini', 'B🔴', 'Florida', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.02);
SELECT insert_connection('Florida', 'B🔴', 'Carlos Pellegrini', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.02);

SELECT insert_connection('Florida', 'B🔴', 'Leandro N. Alem', 'B🔴', 'Leandro N. Alem', 'B🔴', 2.02);
SELECT insert_connection('Leandro N. Alem', 'B🔴', 'Florida', 'B🔴', 'Juan Manuel de Rosas Villa Urquiza', 'B🔴', 2.02);