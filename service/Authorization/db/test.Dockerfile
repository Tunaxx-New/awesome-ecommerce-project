FROM mysql:latest

ENV MYSQL_ROOT_HOST=localhost
ENV MYSQL_ROOT_PASSWORD=admin
ENV MYSQL_DATABASE=db
ENV MYSQL_USER=admin
ENV MYSQL_PASSWORD=admin

EXPOSE 3306

COPY ./init-scripts/*.sql /docker-entrypoint-initdb.d/

CMD ["mysqld"]