CREATE USER schemaregistrybackup WITH PASSWORD 'schemaregistrybackup' CREATEDB;
CREATE DATABASE schemaregistrybackup
    WITH
    OWNER = schemaregistrybackup
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_DK.UTF-8'
    LC_CTYPE = 'en_DK.UTF-8'
    TABLESPACE = pg_default
    CONNECTION_LIMIT = -1;
