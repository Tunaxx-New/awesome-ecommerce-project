-- Create a database
CREATE DATABASE greenshop-db;

-- Switch to the new database
USE greenshop-db;

-- Create a user and grant privileges
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin'; -- Create user
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost'; -- Grant all privileges
FLUSH PRIVILEGES; -- Refresh privileges