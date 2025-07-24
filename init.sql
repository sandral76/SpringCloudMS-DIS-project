IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'clinic_user')
BEGIN
    CREATE LOGIN clinic_user WITH PASSWORD = 'Password123!';
END
GO

IF DB_ID('patient_db') IS NULL
BEGIN
    CREATE DATABASE patient_db;
END
GO

IF DB_ID('doctor_db') IS NULL
BEGIN
    CREATE DATABASE doctor_db;
END
GO

IF DB_ID('appointment_db') IS NULL
BEGIN
    CREATE DATABASE appointment_db;
END
GO

IF DB_ID('billing_db') IS NULL
BEGIN
    CREATE DATABASE billing_db;
END
GO

IF DB_ID('user_db') IS NULL
BEGIN
    CREATE DATABASE user_db;
END
GO

-- f-ja da kreira korisnika u bazi i dodeli mu ulogu db_owner
CREATE PROCEDURE CreateUserIfNotExists
    @dbname SYSNAME,
    @username SYSNAME
AS
BEGIN
    DECLARE @sql NVARCHAR(MAX);
    SET @sql = '
    USE ' + QUOTENAME(@dbname) + ';
    IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = ''' + @username + ''')
    BEGIN
        CREATE USER ' + QUOTENAME(@username) + ' FOR LOGIN ' + QUOTENAME(@username) + ';
        ALTER ROLE db_owner ADD MEMBER ' + QUOTENAME(@username) + ';
    END';
    EXEC sp_executesql @sql;
END
GO

EXEC CreateUserIfNotExists 'patient_db', 'clinic_user';
EXEC CreateUserIfNotExists 'doctor_db', 'clinic_user';
EXEC CreateUserIfNotExists 'appointment_db', 'clinic_user';
EXEC CreateUserIfNotExists 'billing_db', 'clinic_user';
EXEC CreateUserIfNotExists 'user_db', 'clinic_user';
GO

DROP PROCEDURE CreateUserIfNotExists;
GO
