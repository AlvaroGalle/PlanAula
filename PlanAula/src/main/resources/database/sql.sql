CREATE TABLE asignaturas
(
    id         SERIAL PRIMARY KEY,
    asignatura VARCHAR(20) NOT NULL
);

CREATE TABLE cursos
(
    id    SERIAL PRIMARY KEY,
    curso VARCHAR(20) NOT NULL
);

CREATE TABLE profesores
(
    id        SERIAL PRIMARY KEY,
    nombre    VARCHAR(20) NOT NULL,
    apellidos VARCHAR(20)
);

CREATE TABLE tutores
(
    id          SERIAL PRIMARY KEY,
    id_profesor INT,
    id_curso    INT,
    anio        VARCHAR(20),
    CONSTRAINT fk_profesor_tutor
        FOREIGN KEY (id_profesor) REFERENCES profesores (id),
    CONSTRAINT fk_curso_tutor
        FOREIGN KEY (id_curso) REFERENCES cursos (id)
);

CREATE TABLE horas
(
    id   SERIAL PRIMARY KEY,
    hora VARCHAR(20) NOT NULL
);

CREATE TABLE dias
(
    id  SERIAL PRIMARY KEY,
    dia VARCHAR(20) NOT NULL
);

CREATE TABLE aulas
(
    id   SERIAL PRIMARY KEY,
    aula VARCHAR(20) NOT NULL
);

CREATE TABLE guardias
(
    id          SERIAL PRIMARY KEY,
    id_profesor INT,
    id_dia      INT,
    id_hora     INT,
    CONSTRAINT fk_profesor_guardia
        FOREIGN KEY (id_profesor) REFERENCES profesores (id),
    CONSTRAINT fk_dia_guardia
        FOREIGN KEY (id_dia) REFERENCES dias (id),
    CONSTRAINT fk_hora_guardia
        FOREIGN KEY (id_hora) REFERENCES horas (id)
);

CREATE TABLE libranzas
(
    id          SERIAL PRIMARY KEY,
    id_profesor INT,
    id_dia      INT,
    id_hora     INT,
    CONSTRAINT fk_profesor_libranza
        FOREIGN KEY (id_profesor) REFERENCES profesores (id),
    CONSTRAINT fk_dia_libranza
        FOREIGN KEY (id_dia) REFERENCES dias (id),
    CONSTRAINT fk_hora_libranza
        FOREIGN KEY (id_hora) REFERENCES horas (id)
);

CREATE TABLE recreos
(
    id          SERIAL PRIMARY KEY,
    id_profesor INT,
    id_dia      INT,
    id_hora     INT,
    CONSTRAINT fk_profesor_recreo
        FOREIGN KEY (id_profesor) REFERENCES profesores (id),
    CONSTRAINT fk_dia_recreo
        FOREIGN KEY (id_dia) REFERENCES dias (id),
    CONSTRAINT fk_hora_recreo
        FOREIGN KEY (id_hora) REFERENCES horas (id)
);

CREATE TABLE horarios
(
    id            SERIAL PRIMARY KEY,
    id_curso      INT,
    id_profesor   INT,
    id_asignatura INT,
    id_aula       INT,
    id_dia        INT,
    id_hora       INT,
    observaciones TEXT,
    CONSTRAINT fk_cursos
        FOREIGN KEY (id_curso) REFERENCES cursos (id),
    CONSTRAINT fk_profesor_horario
        FOREIGN KEY (id_profesor) REFERENCES profesores (id),
    CONSTRAINT fk_asignatura
        FOREIGN KEY (id_asignatura) REFERENCES asignaturas (id),
    CONSTRAINT fk_aula
        FOREIGN KEY (id_aula) REFERENCES aulas (id),
    CONSTRAINT fk_dia
        FOREIGN KEY (id_dia) REFERENCES dias (id),
    CONSTRAINT fk_hora
        FOREIGN KEY (id_hora) REFERENCES horas (id)
);

CREATE TABLE alumnos
(
    id        SERIAL PRIMARY KEY,
    nombre    VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    id_curso  INT         NOT NULL,
    CONSTRAINT fk_alumno_curso
        FOREIGN KEY (id_curso) REFERENCES cursos (id)
);

CREATE TABLE usuarios
(
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(50) UNIQUE NOT NULL,
    password    VARCHAR(255)       NOT NULL,
    role        VARCHAR(20)        NOT NULL CHECK (role IN ('admin', 'profesor', 'alumno')),
    id_profesor INT NULL,
    id_alumno   INT NULL,
    CONSTRAINT fk_usuario_profesor
        FOREIGN KEY (id_profesor) REFERENCES profesores (id),
    CONSTRAINT fk_usuario_alumno
        FOREIGN KEY (id_alumno) REFERENCES alumnos (id),
    CONSTRAINT chk_usuario_roles CHECK (
        (role = 'profesor' AND id_profesor IS NOT NULL AND id_alumno IS NULL)
            OR (role = 'alumno' AND id_alumno IS NOT NULL AND id_profesor IS NULL)
            OR (role = 'admin' AND id_profesor IS NULL AND id_alumno IS NULL)
        )
);

INSERT INTO asignaturas (asignatura)
VALUES ('Matematicas'),
       ('Lengua'),
       ('Historia'),
       ('Ciencias'),
       ('Ingles'),
       ('Fisica'),
       ('Quimica'),
       ('Biologia'),
       ('Geografia'),
       ('EducacionFisica');

INSERT INTO cursos (curso)
VALUES ('1 ESO'),
       ('2 ESO'),
       ('3 ESO'),
       ('4 ESO'),
       ('1 Bachillerato'),
       ('2 Bachillerato'),
       ('1 Grado Medio'),
       ('2 Grado Medio');

INSERT INTO profesores (nombre, apellidos)
VALUES ('Juan', 'Perez'),
       ('Ana', 'Gonzalez'),
       ('Carlos', 'Lopez'),
       ('Marta', 'Ramirez'),
       ('Luis', 'Sanchez'),
       ('Elena', 'Martinez'),
       ('Pedro', 'Diaz'),
       ('Julia', 'Fernandez'),
       ('Raul', 'Gomez'),
       ('Sofia', 'Moreno');

INSERT INTO tutores (id_profesor, id_curso, anio)
VALUES (1, 1, '23/24'),
       (2, 2, '23/24'),
       (3, 3, '23/24'),
       (4, 4, '24/25'),
       (5, 5, '24/25'),
       (6, 6, '24/25'),
       (7, 7, '24/25'),
       (8, 8, '23/24');

INSERT INTO horas (hora)
VALUES ('08:00'),
       ('09:00'),
       ('10:00'),
       ('11:00'),
       ('12:00'),
       ('13:00'),
       ('14:00'),
       ('15:00'),
       ('16:00'),
       ('17:00');

INSERT INTO dias (dia)
VALUES ('Lunes'),
       ('Martes'),
       ('Miercoles'),
       ('Jueves'),
       ('Viernes'),
       ('Sabado');

INSERT INTO aulas (aula)
VALUES ('Aula 101'),
       ('Aula 102'),
       ('Aula 103'),
       ('Aula 104'),
       ('Aula 105'),
       ('Aula 106'),
       ('Aula 107'),
       ('Aula 108'),
       ('Aula 109'),
       ('Aula 110');

INSERT INTO guardias (id_profesor, id_dia, id_hora)
VALUES (1, 1, 2),
       (2, 2, 3),
       (3, 3, 4),
       (4, 4, 5),
       (5, 5, 1),
       (6, 6, 2),
       (7, 1, 3),
       (8, 2, 4),
       (9, 3, 5),
       (10, 4, 1);

INSERT INTO libranzas (id_profesor, id_dia, id_hora)
VALUES (1, 2, 3),
       (2, 3, 4),
       (3, 4, 5),
       (4, 5, 1),
       (5, 1, 2),
       (6, 2, 3),
       (7, 3, 4),
       (8, 4, 5),
       (9, 5, 1),
       (10, 1, 2);

INSERT INTO recreos (id_profesor, id_dia, id_hora)
VALUES (1, 1, 5),
       (2, 2, 1),
       (3, 3, 2),
       (4, 4, 3),
       (5, 5, 4),
       (6, 1, 2),
       (7, 2, 3),
       (8, 3, 4),
       (9, 4, 5),
       (10, 5, 1);

INSERT INTO horarios (id_curso, id_profesor, id_asignatura, id_aula, id_dia, id_hora, observaciones)
VALUES (1, 1, 1, 1, 1, 1, 'Clase de matematicas'),
       (2, 2, 2, 2, 2, 2, 'Clase de lengua'),
       (3, 3, 3, 3, 3, 3, 'Clase de historia'),
       (4, 4, 4, 4, 4, 4, 'Clase de ciencias'),
       (5, 5, 5, 5, 5, 5, 'Clase de ingles'),
       (6, 6, 6, 6, 1, 1, 'Clase de fisica'),
       (7, 7, 7, 7, 2, 2, 'Clase de quimica'),
       (8, 8, 8, 8, 3, 3, 'Clase de biologia'),
       (9, 9, 9, 9, 4, 4, 'Clase de geografia'),
       (10, 10, 10, 10, 5, 5, 'Clase de educacion fisica');

INSERT INTO alumnos (nombre, apellidos, id_curso) VALUES
                                                      ('Laura', 'Domínguez', 1),
                                                      ('Miguel', 'Ruiz',      2),
                                                      ('Clara',  'Vega',      3);

INSERT INTO usuarios (username, password, role) VALUES
                                                    ('root',    'hash_seguro_de_root',        'admin'),
                                                    ('juanp',   'hash_seguro_de_juanp',       'profesor'),
                                                    ('ana_g',   'hash_seguro_de_anag',        'profesor');

UPDATE usuarios SET id_profesor = 1 WHERE username = 'juanp';
UPDATE usuarios SET id_profesor = 2 WHERE username = 'ana_g';

INSERT INTO usuarios (username, password, role, id_alumno) VALUES
                                                               ('laurad',  'hash_seguro_de_laurad',  'alumno', 1),
                                                               ('miguelr', 'hash_seguro_de_miguelr','alumno', 2);