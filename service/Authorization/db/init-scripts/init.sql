-- Create a database
CREATE DATABASE `db`;

-- Switch to the new database
USE `db`;

-- Create a admin and grant privileges
CREATE USER 'admin'@'172.28.0.1' IDENTIFIED BY 'admin'; -- Create user 172.28.0.1 is?
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'172.28.0.1'; -- Grant all privileges

-- Create a flyway and grant privileges
CREATE USER 'flyway'@'172.28.0.1' IDENTIFIED BY 'flyway';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER, REFERENCES
ON `db`.* TO 'flyway'@'172.28.0.1';

-- Create public user
CREATE USER 'public'@'172.28.0.1';
GRANT SELECT ON `db`._roles TO 'public'@'172.28.0.1';

FLUSH PRIVILEGES; -- Refresh privileges