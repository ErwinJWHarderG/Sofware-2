CREATE DATABASE IF NOT EXISTS biblioteca_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_db;

-- Tabla de Usuarios (Estudiantes / Docentes)
CREATE TABLE IF NOT EXISTS usuarios (
    identificacion VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL
);

-- Tabla de Bibliotecarios
CREATE TABLE IF NOT EXISTS bibliotecarios (
    identificacion VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    turno VARCHAR(20) NOT NULL
);

-- Datos Semilla
INSERT IGNORE INTO usuarios VALUES 
('1001', 'Juan', 'Pérez', 'juan@uniquindio.edu.co', 'ESTUDIANTE', 'ACTIVO'),
('1002', 'Ana', 'Gómez', 'ana@uniquindio.edu.co', 'ESTUDIANTE', 'ACTIVO'),
('1003', 'Carlos', 'Ruiz', 'carlos@uniquindio.edu.co', 'DOCENTE', 'ACTIVO'),
('1004', 'Laura', 'Martínez', 'laura@uniquindio.edu.co', 'ESTUDIANTE', 'INACTIVO'),
('1005', 'Mateo', 'López', 'mateo@uniquindio.edu.co', 'ESTUDIANTE', 'ACTIVO');

INSERT IGNORE INTO bibliotecarios VALUES 
('2001', 'Marta', 'López', 'Bibliotecaria Jefe', 'MANANA'),
('2002', 'Pedro', 'Ramírez', 'Auxiliar de biblioteca', 'TARDE');