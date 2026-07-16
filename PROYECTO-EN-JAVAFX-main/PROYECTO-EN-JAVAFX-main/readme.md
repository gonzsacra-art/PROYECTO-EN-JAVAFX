Cuentas de prueba
 Usuario   Contraseña   Rol      

 admin      admin123    ADMIN    
 operador   oper123     OPERADOR
 El ADMIN ve Empleados, Departamentos, Bonos y Horas Extra.
 El OPERADOR solo ve Empleados y Horas Extra.
 

Script para la base de datos:

IF DB_ID('BD_GestionPlanillas') IS NULL
CREATE DATABASE BD_GestionPlanillas;
GO

USE master;
GO
IF NOT EXISTS (SELECT name FROM sys.sql_logins WHERE name = 'admin')
CREATE LOGIN [admin] WITH PASSWORD = 'admin1234', CHECK_POLICY = OFF;
ELSE
BEGIN
ALTER LOGIN [admin] WITH PASSWORD = 'admin1234';
ALTER LOGIN [admin] ENABLE;
END
GO

USE BD_GestionPlanillas;
GO
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'admin')
CREATE USER [admin] FOR LOGIN [admin];
GO
ALTER ROLE db_owner ADD MEMBER [admin];
GO


IF OBJECT_ID('dbo.Usuarios', 'U') IS NULL
CREATE TABLE Usuarios (
Usuario    VARCHAR(50) NOT NULL,
Contrasena VARCHAR(50) NOT NULL,
Rol        VARCHAR(20) NOT NULL,

    CONSTRAINT PK_Usuarios PRIMARY KEY (Usuario),
    CONSTRAINT CK_Usuarios_Rol CHECK (Rol IN ('ADMIN', 'OPERADOR'))
);
GO


IF OBJECT_ID('dbo.Departamentos', 'U') IS NULL
CREATE TABLE Departamentos (
Id          VARCHAR(20)  NOT NULL,
Nombre      VARCHAR(100) NOT NULL,
Descripcion VARCHAR(200) NULL,
Jefe        VARCHAR(100) NOT NULL,

    CONSTRAINT PK_Departamentos PRIMARY KEY (Id),
    CONSTRAINT UQ_Departamentos_Nombre UNIQUE (Nombre)
);
GO

IF OBJECT_ID('dbo.Empleados', 'U') IS NULL
CREATE TABLE Empleados (
Id             VARCHAR(20)   NOT NULL,
Nombre         VARCHAR(100)  NOT NULL,
Cargo          VARCHAR(60)   NOT NULL,
Sueldo         DECIMAL(10,2) NOT NULL,
Regimen        VARCHAR(10)   NOT NULL,   -- 'AFP' (12%) u 'ONP' (13%)
DepartamentoId VARCHAR(20)   NULL,

    CONSTRAINT PK_Empleados PRIMARY KEY (Id),
    CONSTRAINT CK_Empleados_Sueldo  CHECK (Sueldo > 0),
    CONSTRAINT CK_Empleados_Regimen CHECK (Regimen IN ('AFP', 'ONP')),
    CONSTRAINT FK_Empleados_Departamentos
        FOREIGN KEY (DepartamentoId) REFERENCES Departamentos(Id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
GO

IF OBJECT_ID('dbo.Bonos', 'U') IS NULL
CREATE TABLE Bonos (
Id         VARCHAR(20)   NOT NULL,
EmpleadoId VARCHAR(20)   NOT NULL,
Tipo       VARCHAR(30)   NOT NULL,
Monto      DECIMAL(10,2) NOT NULL,
Fecha      DATE          NOT NULL CONSTRAINT DF_Bonos_Fecha DEFAULT (CAST(GETDATE() AS DATE)),

    CONSTRAINT PK_Bonos PRIMARY KEY (Id),
    CONSTRAINT CK_Bonos_Monto CHECK (Monto > 0),
    CONSTRAINT CK_Bonos_Tipo  CHECK (Tipo IN
        ('PRODUCTIVIDAD', 'ESCOLARIDAD', 'ASIGNACION_FAMILIAR', 'OTRO')),

    CONSTRAINT FK_Bonos_Empleados
        FOREIGN KEY (EmpleadoId) REFERENCES Empleados(Id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO

IF OBJECT_ID('dbo.HorasExtra', 'U') IS NULL
CREATE TABLE HorasExtra (
Id              VARCHAR(20)   NOT NULL,
EmpleadoId      VARCHAR(20)   NOT NULL,
Fecha           DATE          NOT NULL,
HorasTrabajadas INT           NOT NULL,
ValorHoraBase   DECIMAL(10,2) NOT NULL,
Motivo          VARCHAR(200)  NULL,

    CONSTRAINT PK_HorasExtra PRIMARY KEY (Id),
    CONSTRAINT CK_HorasExtra_Horas CHECK (HorasTrabajadas > 0),
    CONSTRAINT CK_HorasExtra_Valor CHECK (ValorHoraBase > 0),

    CONSTRAINT FK_HorasExtra_Empleados
        FOREIGN KEY (EmpleadoId) REFERENCES Empleados(Id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Empleados_DepartamentoId')
CREATE INDEX IX_Empleados_DepartamentoId ON Empleados(DepartamentoId);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Bonos_EmpleadoId')
CREATE INDEX IX_Bonos_EmpleadoId ON Bonos(EmpleadoId);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_HorasExtra_EmpleadoId')
CREATE INDEX IX_HorasExtra_EmpleadoId ON HorasExtra(EmpleadoId);
GO


-- DATOS DE PRUEBA (solo si las tablas estan vacias)

IF NOT EXISTS (SELECT 1 FROM Usuarios)
INSERT INTO Usuarios (Usuario, Contrasena, Rol) VALUES
('admin',    'admin123', 'ADMIN'),
('operador', 'oper123',  'OPERADOR');
GO

IF NOT EXISTS (SELECT 1 FROM Departamentos)
INSERT INTO Departamentos (Id, Nombre, Descripcion, Jefe) VALUES
('D001', 'Contabilidad',      'Gestion contable y financiera', 'Carlos Mendoza'),
('D002', 'Recursos Humanos',  'Gestion del personal',          'Ana Quispe'),
('D003', 'Operaciones',       'Produccion y logistica',        'Jorge Salas');
GO

IF NOT EXISTS (SELECT 1 FROM Empleados)
INSERT INTO Empleados (Id, Nombre, Cargo, Sueldo, Regimen, DepartamentoId) VALUES
('E001', 'Juan Perez',   'Analista',  2500.00, 'AFP', 'D001'),
('E002', 'Maria Torres', 'Contadora', 3200.00, 'ONP', 'D001'),
('E003', 'Luis Ramos',   'Operario',  1500.00, 'AFP', 'D003'),
('E004', 'Rosa Flores',  'Asistente', 1800.00, 'ONP', 'D002'),
('E005', 'Pedro Castro', 'Operario',  1600.00, 'AFP', NULL);  -- sin depto asignado
GO

IF NOT EXISTS (SELECT 1 FROM Bonos)
INSERT INTO Bonos (Id, EmpleadoId, Tipo, Monto, Fecha) VALUES
('B001', 'E001', 'PRODUCTIVIDAD',       300.00, '2026-07-01'),
('B002', 'E002', 'ESCOLARIDAD',         400.00, '2026-07-01'),
('B003', 'E003', 'ASIGNACION_FAMILIAR', 102.50, '2026-07-05');
GO

IF NOT EXISTS (SELECT 1 FROM HorasExtra)
INSERT INTO HorasExtra (Id, EmpleadoId, Fecha, HorasTrabajadas, ValorHoraBase, Motivo) VALUES
('H001', 'E001', '2026-07-10', 3, 10.42, 'Cierre contable mensual'),
('H002', 'E003', '2026-07-11', 2,  6.25, 'Mantenimiento de maquinaria');
GO


SELECT 'Usuarios'      AS Tabla, COUNT(*) AS Registros FROM Usuarios
UNION ALL SELECT 'Departamentos', COUNT(*) FROM Departamentos
UNION ALL SELECT 'Empleados',     COUNT(*) FROM Empleados
UNION ALL SELECT 'Bonos',         COUNT(*) FROM Bonos
UNION ALL SELECT 'HorasExtra',    COUNT(*) FROM HorasExtra;
GO
