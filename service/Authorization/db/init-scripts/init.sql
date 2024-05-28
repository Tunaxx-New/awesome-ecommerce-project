-- Create a database
CREATE DATABASE `db`;

-- Switch to the new database
USE `db`;

-- Create a admin and grant privileges
CREATE USER 'admin'@'%' IDENTIFIED BY 'admin'; -- Create user % is?
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%'; -- Grant all privileges

-- Create a flyway and grant privileges
CREATE USER 'flyway'@'%' IDENTIFIED BY 'flyway';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER, REFERENCES
ON `db`.* TO 'flyway'@'%';

-- Create public user
CREATE USER 'public'@'%';
GRANT SELECT ON `db`._roles TO 'public'@'%';

FLUSH PRIVILEGES; -- Refresh privileges