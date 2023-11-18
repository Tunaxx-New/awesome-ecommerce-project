-- Create a database
CREATE DATABASE `${MYSQL_DATABASE}`;

-- Switch to the new database
USE `${MYSQL_DATABASE}`;

-- Create a user and grant privileges
CREATE USER '${MYSQL_USER}'@'${MYSQL_ROOT_HOST}' IDENTIFIED BY '${MYSQL_PASSWORD}'; -- Create user
GRANT ALL PRIVILEGES ON *.* TO '${MYSQL_USER}'@'${MYSQL_ROOT_HOST}'; -- Grant all privileges
FLUSH PRIVILEGES; -- Refresh privileges