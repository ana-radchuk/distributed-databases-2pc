FROM postgres:13

COPY my-schema.sql /docker-entrypoint-initdb.d/my-schema.sql

RUN chmod +x /docker-entrypoint-initdb.d/my-schema.sql
