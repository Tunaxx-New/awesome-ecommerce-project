#!/bin/bash
echo "TUNAXX"
mysql -u root -p"$MYSQL_ROOT_PASSWORD" < "/docker-entrypoint-initdb.d/init.sql"