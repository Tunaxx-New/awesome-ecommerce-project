-- Create a database
CREATE DATABASE `${MYSQL_DATABASE}`;

-- Switch to the new database
USE `${MYSQL_DATABASE}`;

-- Create a admin and grant privileges
CREATE USER '${MYSQL_ADMIN}'@'${MYSQL_ROOT_HOST}' IDENTIFIED BY '${MYSQL_ADMIN_PASSWORD}'; -- Create user
GRANT ALL PRIVILEGES ON *.* TO '${MYSQL_ADMIN}'@'${MYSQL_ROOT_HOST}'; -- Grant all privileges

-- Create a flyway and grant privileges
CREATE USER '${MYSQL_FLYWAY_USER}'@'${MYSQL_ROOT_HOST}';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER
ON `${MYSQL_DATABASE}`.* TO '${MYSQL_FLYWAY_USER}'@'${MYSQL_ROOT_HOST}' IDENTIFIED BY '${MYSQL_FLYWAY_PASSWORD}';

-- Create public user
CREATE USER '${MYSQL_PUBLIC_USER}'@'${MYSQL_ROOT_HOST}';
GRANT SELECT ON `${MYSQL_DATABASE}`._roles TO '${MYSQL_PUBLIC_USER}'@'${MYSQL_ROOT_HOST}';

FLUSH PRIVILEGES; -- Refresh privileges