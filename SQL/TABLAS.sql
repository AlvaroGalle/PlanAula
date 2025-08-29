DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS tutores CASCADE;
DROP TABLE IF EXISTS libranzas CASCADE;
DROP TABLE IF EXISTS recreos CASCADE;
DROP TABLE IF EXISTS guardias CASCADE;
DROP TABLE IF EXISTS horarios CASCADE;
DROP TABLE IF EXISTS alumnos CASCADE;
DROP TABLE IF EXISTS profesores CASCADE;
DROP TABLE IF EXISTS horas CASCADE;
DROP TABLE IF EXISTS dias CASCADE;
DROP TABLE IF EXISTS cursos CASCADE;
DROP TABLE IF EXISTS aulas CASCADE;
DROP TABLE IF EXISTS asignaturas CASCADE;

CREATE TABLE asignaturas (
  id SERIAL PRIMARY KEY,
  asignatura VARCHAR NOT NULL
);

CREATE TABLE aulas (
  id SERIAL PRIMARY KEY,
  aula VARCHAR NOT NULL
);

CREATE TABLE cursos (
  id SERIAL PRIMARY KEY,
  curso VARCHAR NOT NULL
);

CREATE TABLE dias (
  id SERIAL PRIMARY KEY,
  dia VARCHAR NOT NULL
);

CREATE TABLE horas (
  id SERIAL PRIMARY KEY,
  hora VARCHAR NOT NULL
);

CREATE TABLE profesores (
  id SERIAL PRIMARY KEY,
  nombre VARCHAR NOT NULL,
  apellidos VARCHAR
);

CREATE TABLE alumnos (
  id SERIAL PRIMARY KEY,
  nombre VARCHAR NOT NULL,
  apellidos VARCHAR NOT NULL,
  id_curso INTEGER NOT NULL,
  CONSTRAINT fk_alumno_curso
    FOREIGN KEY (id_curso) REFERENCES cursos(id)
);

CREATE TABLE horarios (
  id SERIAL PRIMARY KEY,
  id_curso INTEGER,
  id_profesor INTEGER,
  id_asignatura INTEGER,
  id_aula INTEGER,
  id_dia INTEGER,
  id_hora INTEGER,
  observaciones TEXT,
  CONSTRAINT fk_cursos FOREIGN KEY (id_curso) REFERENCES cursos(id),
  CONSTRAINT fk_profesor_horario FOREIGN KEY (id_profesor) REFERENCES profesores(id),
  CONSTRAINT fk_asignatura FOREIGN KEY (id_asignatura) REFERENCES asignaturas(id),
  CONSTRAINT fk_aula FOREIGN KEY (id_aula) REFERENCES aulas(id),
  CONSTRAINT fk_dia FOREIGN KEY (id_dia) REFERENCES dias(id),
  CONSTRAINT fk_hora FOREIGN KEY (id_hora) REFERENCES horas(id)
);

CREATE TABLE guardias (
  id SERIAL PRIMARY KEY,
  id_profesor INTEGER,
  id_dia INTEGER,
  id_hora INTEGER,
  CONSTRAINT fk_profesor_guardia FOREIGN KEY (id_profesor) REFERENCES profesores(id),
  CONSTRAINT fk_dia_guardia FOREIGN KEY (id_dia) REFERENCES dias(id),
  CONSTRAINT fk_hora_guardia FOREIGN KEY (id_hora) REFERENCES horas(id)
);

CREATE TABLE recreos (
  id SERIAL PRIMARY KEY,
  id_profesor INTEGER,
  id_dia INTEGER,
  id_hora INTEGER,
  CONSTRAINT fk_profesor_recreo FOREIGN KEY (id_profesor) REFERENCES profesores(id),
  CONSTRAINT fk_dia_recreo FOREIGN KEY (id_dia) REFERENCES dias(id),
  CONSTRAINT fk_hora_recreo FOREIGN KEY (id_hora) REFERENCES horas(id)
);

CREATE TABLE libranzas (
  id SERIAL PRIMARY KEY,
  id_profesor INTEGER,
  id_dia INTEGER,
  id_hora INTEGER,
  CONSTRAINT fk_profesor_libranza FOREIGN KEY (id_profesor) REFERENCES profesores(id),
  CONSTRAINT fk_dia_libranza FOREIGN KEY (id_dia) REFERENCES dias(id),
  CONSTRAINT fk_hora_libranza FOREIGN KEY (id_hora) REFERENCES horas(id)
);

CREATE TABLE tutores (
  id SERIAL PRIMARY KEY,
  id_profesor INTEGER,
  id_curso INTEGER,
  anio VARCHAR,
  CONSTRAINT fk_profesor_tutor FOREIGN KEY (id_profesor) REFERENCES profesores(id),
  CONSTRAINT fk_curso_tutor FOREIGN KEY (id_curso) REFERENCES cursos(id)
);

CREATE TABLE usuarios (
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL,
  role VARCHAR NOT NULL,
  id_profesor INTEGER,
  id_alumno INTEGER,
  CONSTRAINT usuarios_role_check
    CHECK (role IN ('admin','profesor','alumno')),
  CONSTRAINT chk_usuario_roles
    CHECK (
      (role = 'profesor' AND id_profesor IS NOT NULL AND id_alumno IS NULL) OR
      (role = 'alumno'   AND id_alumno IS NOT NULL   AND id_profesor IS NULL) OR
      (role = 'admin'    AND id_profesor IS NULL     AND id_alumno IS NULL)
    ),
  CONSTRAINT fk_usuario_profesor FOREIGN KEY (id_profesor) REFERENCES profesores(id),
  CONSTRAINT fk_usuario_alumno FOREIGN KEY (id_alumno) REFERENCES alumnos(id)
);