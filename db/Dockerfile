# Use the MySQL image version 8.3 (allows native password plugin)
FROM mysql:8.3
# Set the working directory
WORKDIR /tmp

COPY world-db/world.sql /docker-entrypoint-initdb.d
# Set the root password
ENV MYSQL_ROOT_PASSWORD world