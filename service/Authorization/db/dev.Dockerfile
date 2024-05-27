FROM mysql:8.4.0

# Copy custom MySQL configuration if needed
COPY mysqld.cnf /etc/mysql/conf.d/default-auth-override.cnf
COPY mysqld.cnf /etc/mysql/conf.d/mysqld.cnf

EXPOSE 3306

COPY ./init-scripts/*.sql /docker-entrypoint-initdb.d/
COPY ./init-scripts/entrypoint.sh /docker-entrypoint-initdb.d/
COPY ./init-scripts/*.sh /etc/

RUN chmod -R 755 /docker-entrypoint-initdb.d/

# Start the MySQL server and run mysql_upgrade
CMD ["mysqld"]