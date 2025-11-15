-- TABLA CLIENTES
INSERT INTO cliente (id, rut, nombre, email, telefono) VALUES
(1, '12345678-9', 'Juan Pérez', 'juan@email.com', '+56912345678'),
(2, '98765432-1', 'María García', 'maria@email.com', '+56987654321'),
(3, '11223344-5', 'Carlos López', 'carlos@email.com', '+56911223344'),
(4, '55667788-9', 'Ana Martínez', 'ana@email.com', '+56955667788'),
(5, '22334455-6', 'Roberto Silva', 'roberto@email.com', '+56922334455');

-- TABLA MESAS
INSERT INTO mesa (id, numero, capacidad, disponible) VALUES
(1, 1, 2, true),
(2, 2, 2, true),
(3, 3, 4, true),
(4, 4, 4, true),
(5, 5, 6, true),
(6, 6, 6, true),
(7, 7, 8, true),
(8, 8, 8, true);

-- TABLA RESERVAS
INSERT INTO reserva (id, cliente_id, mesa_id, fecha_hora) VALUES
(1, 1, 3, '2025-11-20 19:00:00'),
(2, 2, 5, '2025-11-21 20:00:00'),
(3, 3, 1, '2025-11-22 19:30:00'),
(4, 4, 7, '2025-11-23 18:00:00'),
(5, 5, 2, '2025-11-24 20:30:00');