select *from peliculas;
select *from funciones;
select *from boletos;
select *from clientes;
select *from compras;
select *from empleados;
select *from funciones;
select *from peliculas;
select *from roles;
select *from salas;
select *from usuarios;
select *from usuarios_roles;
select *from ventas;

-- Creacion de la base de datos
CREATE DATABASE SistemaCine;

-- Le indicamos al manejador que usaremos las base de datos SistemaCine
USE SistemaCine;

-- Creacion de la tabla peliculas
CREATE TABLE peliculas (
-- La llave primaria sera autoincremental
    pelicula_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    duracion INT NOT NULL,
    sinopsis TEXT,
    clasificacion VARCHAR(10)
);

-- Creacion de los generos de peliculas
CREATE TABLE generos (
    genero_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_genero VARCHAR(100) NOT NULL
);


-- Creamos esta tabla para usarla de puente en la base de datos
CREATE TABLE peliculas_genero (
    pelicula_id INT,
    genero_id INT,
    
    -- Usare llave compuesta
    PRIMARY KEY (pelicula_id, genero_id),
    
    -- Creacion de llave foranea para referenciar la tabla pelicula 
    FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id),
    FOREIGN KEY (genero_id) REFERENCES generos(genero_id)
);
select *from salas;
CREATE TABLE salas (
    sala_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    capacidad INT NOT NULL
);

CREATE TABLE promociones (
    promocion_id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    descuento DECIMAL(5,2) DEFAULT 0.00
);

CREATE TABLE funciones (
    funcion_id INT AUTO_INCREMENT PRIMARY KEY,
    pelicula_id INT,
    sala_id INT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    promocion_id INT,
    FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id),
    FOREIGN KEY (sala_id) REFERENCES salas(sala_id),
    FOREIGN KEY (promocion_id) REFERENCES promociones(promocion_id)
);

CREATE TABLE clientes (
    cliente_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE
);

CREATE TABLE empleados (
    empleado_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cargo VARCHAR(100)
);

CREATE TABLE compras (
    compra_id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    empleado_id INT,
    fecha DATETIME NOT NULL,
    total DECIMAL(10,2),
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(empleado_id)
);

CREATE TABLE boletos (
    boleto_id INT AUTO_INCREMENT PRIMARY KEY,
    compra_id INT,
    funcion_id INT,
    asiento VARCHAR(10),
    precio DECIMAL(10,2),
    FOREIGN KEY (compra_id) REFERENCES compras(compra_id),
    FOREIGN KEY (funcion_id) REFERENCES funciones(funcion_id)
);

-- Insercion de datos en la base de datos
-- Películas
INSERT INTO peliculas (titulo, duracion, sinopsis, clasificacion) VALUES
('Inception', 148, 'Un ladrón roba secretos a través de sueños.', 'PG-13'),
('Titanic', 195, 'Historia de amor en el Titanic.', 'PG-13'),
('Avengers: Endgame', 181, 'Batalla final contra Thanos.', 'PG-13'),
('Coco', 105, 'Niño viaja al mundo de los muertos.', 'PG'),
('The Godfather', 175, 'Mafia y familia.', 'R'),
('Toy Story', 81, 'Juguetes que cobran vida.', 'G'),
('Jurassic Park', 127, 'Dinosaurios clonados.', 'PG-13'),
('The Matrix', 136, 'Realidad simulada.', 'R'),
('Parasite', 132, 'Crítica social y suspenso.', 'R'),
('The Lion King', 88, 'Rey león y su destino.', 'G');

--  Géneros
INSERT INTO generos (nombre_genero) VALUES
('Acción'), ('Drama'), ('Fantasía'), ('Comedia'), ('Animación'),
('Aventura'), ('Romance'), ('Ciencia Ficción'), ('Suspenso'), ('Familiar');

-- Películas-Género
INSERT INTO peliculas_genero (pelicula_id, genero_id) VALUES
(1, 1), (1, 8),
(2, 2), (2, 7),
(3, 1), (3, 6),
(4, 4), (4, 5),
(5, 2), (5, 9),
(6, 5), (6, 4),
(7, 6), (7, 1),
(8, 8), (8, 1),
(9, 2), (9, 9),
(10, 5), (10, 10);

-- Salas
INSERT INTO salas (nombre, tipo, capacidad) VALUES
('Sala 1', '3D', 100),
('Sala 2', '2D', 80),
('Sala 3', 'VIP', 60),
('Sala 4', '2D', 90),
('Sala 5', 'IMAX', 120),
('Sala 6', '3D', 100),
('Sala 7', '2D', 85),
('Sala 8', 'VIP', 50),
('Sala 9', '2D', 70),
('Sala 10', '3D', 100);

-- Promociones
INSERT INTO promociones (descripcion, descuento) VALUES
('Miércoles 2x1', 50.00),
('Promo estudiantes', 25.00),
('Descuento tercera edad', 30.00),
('Viernes Familiar', 20.00),
('Promo pareja', 40.00),
('Noche de clásicos', 15.00),
('Tanda matutina', 10.00),
('Descuento niños', 35.00),
('Promo cumpleañero', 30.00),
('Lunes locos', 45.00);

-- Funciones
INSERT INTO funciones (pelicula_id, sala_id, fecha, hora, promocion_id) VALUES
(1, 1, '2025-06-10', '18:00:00', 1),
(2, 2, '2025-06-10', '20:00:00', 2),
(3, 3, '2025-06-11', '19:30:00', 3),
(4, 4, '2025-06-11', '17:00:00', NULL),
(5, 5, '2025-06-12', '21:00:00', 5),
(6, 6, '2025-06-12', '16:30:00', 6),
(7, 7, '2025-06-13', '18:00:00', NULL),
(8, 8, '2025-06-13', '20:30:00', 7),
(9, 9, '2025-06-14', '19:00:00', 8),
(10, 10, '2025-06-14', '17:30:00', NULL);

-- Clientes
INSERT INTO clientes (nombre, correo) VALUES
('Carlos Gómez', 'carlos@gmail.com'),
('Ana Torres', 'ana@hotmail.com'),
('Luis Pérez', 'luis@gmail.com'),
('María López', 'maria@yahoo.com'),
('Pedro Sánchez', 'pedro@gmail.com'),
('Laura Mendoza', 'laura@hotmail.com'),
('Jorge Ruiz', 'jorge@outlook.com'),
('Daniela Vargas', 'daniela@gmail.com'),
('Fernando Díaz', 'fernando@yahoo.com'),
('Lucía Herrera', 'lucia@hotmail.com');

-- Empleados
INSERT INTO empleados (nombre, cargo) VALUES
('Sandra Morales', 'Cajero'),
('Iván Castro', 'Taquillero'),
('Rebeca Ayala', 'Gerente'),
('Tomás Rivera', 'Cajero'),
('Diana Molina', 'Administrador'),
('Marco Gil', 'Supervisor'),
('Elena Ríos', 'Asistente'),
('Gabriel Luna', 'Seguridad'),
('Valeria Torres', 'Cajero'),
('Ángel Fernández', 'Cajero');

-- Compras
INSERT INTO compras (cliente_id, empleado_id, fecha, total) VALUES
(1, 1, '2025-06-10 17:30:00', 12.00),
(2, 2, '2025-06-10 19:00:00', 24.00),
(3, 3, '2025-06-11 20:00:00', 36.00),
(4, 4, '2025-06-11 16:00:00', 12.00),
(5, 5, '2025-06-12 21:30:00', 24.00),
(6, 6, '2025-06-12 15:30:00', 36.00),
(7, 7, '2025-06-13 18:30:00', 12.00),
(8, 8, '2025-06-13 20:00:00', 24.00),
(9, 9, '2025-06-14 19:15:00', 36.00),
(10, 10, '2025-06-14 17:00:00', 12.00);

-- Boletos
INSERT INTO boletos (compra_id, funcion_id, asiento, precio) VALUES
(1, 1, 'A1', 12.00),
(2, 2, 'B1', 12.00), (2, 2, 'B2', 12.00),
(3, 3, 'C1', 12.00), (3, 3, 'C2', 12.00), (3, 3, 'C3', 12.00),
(4, 4, 'D1', 12.00),
(5, 5, 'E1', 12.00), (5, 5, 'E2', 12.00),
(6, 6, 'F1', 12.00), (6, 6, 'F2', 12.00), (6, 6, 'F3', 12.00),
(7, 7, 'G1', 12.00),
(8, 8, 'H1', 12.00), (8, 8, 'H2', 12.00),
(9, 9, 'I1', 12.00), (9, 9, 'I2', 12.00), (9, 9, 'I3', 12.00),
(10, 10, 'J1', 12.00);

-- Consultas avanzados e insercion

START TRANSACTION;

-- 1. Insertar la compra
INSERT INTO compras (cliente_id, empleado_id, fecha, total)
VALUES (1, 1, NOW(), 0.00);

-- Obtener el ID de la compra recién creada
SET @compra_id = LAST_INSERT_ID();

-- 2. Insertar boletos relacionados a esa compra
INSERT INTO boletos (compra_id, funcion_id, asiento, precio) VALUES
(@compra_id, 1, 'K1', 12.00),
(@compra_id, 1, 'K2', 12.00);

-- 3. Actualizar el total de la compra
UPDATE compras
SET total = (SELECT SUM(precio) FROM boletos WHERE compra_id = @compra_id)
WHERE compra_id = @compra_id;

COMMIT;



DELIMITER //

CREATE TRIGGER verificar_aforo
BEFORE INSERT ON boletos
FOR EACH ROW
BEGIN
  DECLARE capacidad_sala INT;
  DECLARE boletos_vendidos INT;

  -- Obtener la capacidad de la sala
  SELECT s.capacidad INTO capacidad_sala
  FROM funciones f
  JOIN salas s ON f.sala_id = s.sala_id
  WHERE f.funcion_id = NEW.funcion_id;

  -- Contar boletos vendidos para esa función
  SELECT COUNT(*) INTO boletos_vendidos
  FROM boletos
  WHERE funcion_id = NEW.funcion_id;

  -- Verificar aforo
  IF boletos_vendidos >= capacidad_sala THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Capacidad de la sala alcanzada. No se puede vender más boletos.';
  END IF;
END;
//

DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_vender_boletos (
    IN p_cliente_id INT,
    IN p_empleado_id INT,
    IN p_funcion_id INT,
    IN p_asientos TEXT -- Ejemplo: 'A1,A2,A3'
)
BEGIN
    DECLARE v_capacidad INT;
    DECLARE v_boletos_vendidos INT;
    DECLARE v_total DECIMAL(10,2);
    DECLARE v_precio_unitario DECIMAL(10,2) DEFAULT 10.00;
    DECLARE v_compra_id INT;
    DECLARE v_num_asientos INT;

    START TRANSACTION;

    -- Obtener capacidad de la sala
    SELECT s.capacidad INTO v_capacidad
    FROM funciones f
    JOIN salas s ON f.sala_id = s.sala_id
    WHERE f.funcion_id = p_funcion_id;

    -- Contar boletos vendidos actualmente
    SELECT COUNT(*) INTO v_boletos_vendidos
    FROM boletos
    WHERE funcion_id = p_funcion_id;

    -- Contar número de asientos solicitados
    SET v_num_asientos = LENGTH(p_asientos) - LENGTH(REPLACE(p_asientos, ',', '')) + 1;

    -- Verificar si hay aforo suficiente
    IF v_boletos_vendidos + v_num_asientos > v_capacidad THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay suficientes asientos disponibles.';
    END IF;

    -- Insertar compra
    INSERT INTO compras (cliente_id, empleado_id, fecha, total)
    VALUES (p_cliente_id, p_empleado_id, NOW(), 0.00);

    SET v_compra_id = LAST_INSERT_ID();

    -- Insertar boletos (recorrer la lista de asientos)
    WHILE LOCATE(',', p_asientos) > 0 DO
        INSERT INTO boletos (compra_id, funcion_id, asiento, precio)
        VALUES (v_compra_id, p_funcion_id, SUBSTRING_INDEX(p_asientos, ',', 1), v_precio_unitario);
        SET p_asientos = SUBSTRING(p_asientos, LOCATE(',', p_asientos) + 1);
    END WHILE;

    -- Insertar el último asiento (o único si no había coma)
    INSERT INTO boletos (compra_id, funcion_id, asiento, precio)
    VALUES (v_compra_id, p_funcion_id, p_asientos, v_precio_unitario);

    -- Calcular total
    SELECT SUM(precio) INTO v_total
    FROM boletos
    WHERE compra_id = v_compra_id;

    -- Actualizar total de la compra
    UPDATE compras SET total = v_total WHERE compra_id = v_compra_id;

    COMMIT;
END;
//
DELIMITER ;


CALL sp_vender_boletos(1, 1, 2, 'A1,A2,A3');

CREATE OR REPLACE VIEW V_ResumenCompras AS
SELECT 
    C.compra_id,
    CL.nombre AS cliente,
    CL.correo AS correo_cliente,
    E.nombre AS empleado,
    F.funcion_id,
    P.titulo AS pelicula,
    S.nombre AS sala,
    F.fecha AS fecha_funcion, -- corregido aquí
    C.fecha AS fecha_compra,
    C.total AS total_pagado
FROM compras C
JOIN clientes CL ON C.cliente_id = CL.cliente_id
JOIN empleados E ON C.empleado_id = E.empleado_id
JOIN boletos B ON C.compra_id = B.compra_id
JOIN funciones F ON B.funcion_id = F.funcion_id
JOIN peliculas P ON F.pelicula_id = P.pelicula_id
JOIN salas S ON F.sala_id = S.sala_id
GROUP BY 
    C.compra_id, CL.nombre, CL.correo, E.nombre, F.funcion_id,
    P.titulo, S.nombre, F.fecha, C.fecha, C.total;


DESCRIBE funciones;

-- Funciones disponibles por fecha
SELECT F.funcion_id, P.titulo, S.nombre AS sala, F.fecha
FROM funciones F
JOIN peliculas P ON F.pelicula_id = P.pelicula_id
JOIN salas S ON F.sala_id = S.sala_id
WHERE F.fecha = CURDATE(); -- o usa una fecha específica

-- Películas más vistas
SELECT P.titulo, COUNT(B.boleto_id) AS total_boletos
FROM boletos B 
JOIN funciones F ON B.funcion_id = F.funcion_id
JOIN peliculas P ON F.pelicula_id = P.pelicula_id
GROUP BY P.titulo
ORDER BY total_boletos DESC
LIMIT 5;

-- Ingresos por día o semana
SELECT DATE(C.fecha) AS dia, SUM(C.total) AS ingresos
FROM compras C
GROUP BY DATE(C.fecha)
ORDER BY dia DESC;

-- Clientes mas frecuentes
SELECT CL.nombre, CL.correo, COUNT(C.compra_id) AS total_compras
FROM compras C
JOIN clientes CL ON C.cliente_id = CL.cliente_id
GROUP BY CL.cliente_id
ORDER BY total_compras DESC
LIMIT 5;

-- Comparacion entre salas
SELECT S.nombre AS sala, S.capacidad, COUNT(B.boleto_id) AS boletos_vendidos
FROM boletos B
JOIN funciones F ON B.funcion_id = F.funcion_id
JOIN salas S ON F.sala_id = S.sala_id
GROUP BY S.sala_id
ORDER BY boletos_vendidos DESC;

-- Agrupacion por genero de peliculas 
SELECT G.nombre_genero AS genero, COUNT(B.boleto_id) AS total_vistos
FROM boletos B
JOIN funciones F ON B.funcion_id = F.funcion_id
JOIN peliculas P ON F.pelicula_id = P.pelicula_id
JOIN peliculas_genero PG ON P.pelicula_id = PG.pelicula_id
JOIN generos G ON PG.genero_id = G.genero_id
GROUP BY G.nombre_genero
ORDER BY total_vistos DESC;


ALTER TABLE empleados ADD activo BOOLEAN DEFAULT TRUE;
SELECT * FROM empleados;

SELECT DISTINCT s.sala_id, s.nombre
FROM salas s
JOIN funciones f ON s.sala_id = f.sala_id
WHERE f.activo = 1;

select *from funciones;

-- Se creo la tabla ventas, para gestionar la venta de boletos

CREATE TABLE ventas (
    venta_id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    nombre_cliente VARCHAR(100) NOT NULL,
    pelicula VARCHAR(150) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    sala VARCHAR(50) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id)
);

-- se le hizo un cambio para id cliente ya que se ingresa de manera autamtica y se le permitio que sea nulo
ALTER TABLE ventas MODIFY cliente_id INT NULL; 

ALTER TABLE ventas ALTER cliente_id SET DEFAULT NULL;
-- se establecio como nulo


-- Tercer Parcial

-- Crear base de datos
CREATE DATABASE SistemaCine;
USE SistemaCine;

-- Tabla: Películas
CREATE TABLE peliculas (
    pelicula_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    duracion INT NOT NULL,
    sinopsis TEXT,
    clasificacion VARCHAR(10)
);

-- Tabla: Géneros
CREATE TABLE generos (
    genero_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_genero VARCHAR(100) NOT NULL
);

-- Tabla puente: Películas-Género
CREATE TABLE peliculas_genero (
    pelicula_id INT,
    genero_id INT,
    PRIMARY KEY (pelicula_id, genero_id),
    FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id),
    FOREIGN KEY (genero_id) REFERENCES generos(genero_id)
);

-- Tabla: Salas
CREATE TABLE salas (
    sala_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    capacidad INT NOT NULL
);

-- Tabla: Promociones
CREATE TABLE promociones (
    promocion_id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    descuento DECIMAL(5,2) DEFAULT 0.00
);

-- Tabla: Funciones
CREATE TABLE funciones (
    funcion_id INT AUTO_INCREMENT PRIMARY KEY,
    pelicula_id INT,
    sala_id INT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    promocion_id INT,
    FOREIGN KEY (pelicula_id) REFERENCES peliculas(pelicula_id),
    FOREIGN KEY (sala_id) REFERENCES salas(sala_id),
    FOREIGN KEY (promocion_id) REFERENCES promociones(promocion_id)
);

-- Tabla: Clientes
CREATE TABLE clientes (
    cliente_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE
);

-- Tabla: Empleados
CREATE TABLE empleados (
    empleado_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cargo VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla: Compras
CREATE TABLE compras (
    compra_id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    empleado_id INT,
    fecha DATETIME NOT NULL,
    total DECIMAL(10,2),
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(empleado_id)
);

-- Tabla: Boletos
CREATE TABLE boletos (
    boleto_id INT AUTO_INCREMENT PRIMARY KEY,
    compra_id INT,
    funcion_id INT,
    asiento VARCHAR(10),
    precio DECIMAL(10,2),
    FOREIGN KEY (compra_id) REFERENCES compras(compra_id),
    FOREIGN KEY (funcion_id) REFERENCES funciones(funcion_id)
);

-- Tabla: Ventas (resumen amigable para reportes)
CREATE TABLE ventas (
    venta_id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NULL,
    nombre_cliente VARCHAR(100) NOT NULL,
    pelicula VARCHAR(150) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    sala VARCHAR(50) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id)
);

-- ================================================
-- MODULO DE SEGURIDAD
-- ================================================

-- Tabla: Usuarios
CREATE TABLE usuarios (
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    NombreUsuario VARCHAR(50) UNIQUE NOT NULL,
    Contrasenia VARCHAR(255) NOT NULL, -- En netbeans le haremos un hash a la contrasenia
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: Roles
CREATE TABLE roles (
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL
);

-- Tabla puente: Usuarios-Roles
CREATE TABLE usuarios_roles (
    usuario_id INT,
    rol_id INT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (rol_id) REFERENCES roles(rol_id)
);

-- Tabla: Log de accesos/acciones (opcional)
CREATE TABLE login_accesos (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    accion VARCHAR(255),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id)
);


-- Insertar roles
INSERT INTO roles (nombre_rol) VALUES 
('Administrador'), 
('Cajero'), 
('Gerente');

-- Insertar usuarios (hash con SHA2)
INSERT INTO usuarios (NombreUsuario, Contrasenia) 
VALUES 
('admin', SHA2('admin123', 256)), 
('cajero1', SHA2('cajero123', 256));

-- Asignar roles
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES 
(1, 1), -- admin -> Administrador
(2, 2); -- cajero1 -> Cajero
select *from empleados;

ALTER TABLE usuarios ADD COLUMN cambio_contrasenia BOOLEAN DEFAULT FALSE;

ALTER TABLE usuarios ADD COLUMN empleado_id INT;
ALTER TABLE usuarios ADD FOREIGN KEY (empleado_id) REFERENCES empleados(empleado_id);
select *from usuarios;

DESCRIBE empleados;
