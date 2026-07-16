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

IF OBJECT_ID('dbo.Empleados', 'U') IS NULL
CREATE TABLE Empleados (
Id      VARCHAR(20)   NOT NULL PRIMARY KEY,
Nombre  VARCHAR(100)  NOT NULL,
Cargo   VARCHAR(60)   NOT NULL,
Sueldo  DECIMAL(10,2) NOT NULL CONSTRAINT CK_Empleados_Sueldo CHECK (Sueldo > 0),
Regimen VARCHAR(10)   NOT NULL CONSTRAINT CK_Empleados_Regimen CHECK (Regimen IN ('AFP', 'ONP'))
);
GO

IF OBJECT_ID('dbo.Usuarios', 'U') IS NULL
CREATE TABLE Usuarios (
Usuario    VARCHAR(50) NOT NULL PRIMARY KEY,
Contrasena VARCHAR(50) NOT NULL,
Rol        VARCHAR(20) NOT NULL CONSTRAINT CK_Usuarios_Rol CHECK (Rol IN ('ADMIN', 'OPERADOR'))
);
GO

IF NOT EXISTS (SELECT 1 FROM Usuarios)
INSERT INTO Usuarios (Usuario, Contrasena, Rol) VALUES
('admin',    'admin123', 'ADMIN'),
('operador', 'oper123',  'OPERADOR');
GO

IF NOT EXISTS (SELECT 1 FROM Empleados)
INSERT INTO Empleados (Id, Nombre, Cargo, Sueldo, Regimen) VALUES
('E001', 'Juan Perez',   'Analista',  2500.00, 'AFP'),
('E002', 'Maria Torres', 'Contadora', 3200.00, 'ONP'),
('E003', 'Luis Ramos',   'Operario',  1500.00, 'AFP');
GO