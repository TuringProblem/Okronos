FROM mariadb:noble

COPY docker/db_sql/docker-entrypoint-initdb.d/ /docker-entrypoint-initdb.d/
COPY docker/db_sql/docker-entrypoint.sh /usr/local/bin/