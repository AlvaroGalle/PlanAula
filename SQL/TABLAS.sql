TABLAS

CREATE TABLE asignaturas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE aulas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE horarios (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL,
    dia VARCHAR(15) NOT NULL,
    hora TIME NOT NULL
);

CREATE TABLE cursos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE dias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE horas (
    id SERIAL PRIMARY KEY,
    hora TIME NOT NULL
);

CREATE TABLE profesores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE espacios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE planificacion (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(10),
    dia_id INTEGER REFERENCES dias(id),
    hora_id INTEGER REFERENCES horas(id),
    asignatura_id INTEGER REFERENCES asignaturas(id),
    curso_id INTEGER REFERENCES cursos(id),
    espacio_id INTEGER REFERENCES espacios(id),
    profesor_id INTEGER REFERENCES profesores(id),
    sustituto_id INTEGER REFERENCES profesores(id),
    observaciones TEXT
);

CREATE TABLE dias_codificados (
    id SERIAL PRIMARY KEY,
    dia VARCHAR(15),
    codigo VARCHAR(3)
);

CREATE TABLE tutores (
    id SERIAL PRIMARY KEY,
    curso_id INTEGER REFERENCES cursos(id),
    tutor_2425_id INTEGER REFERENCES profesores(id),
    tutor_2324_id INTEGER REFERENCES profesores(id)
);
