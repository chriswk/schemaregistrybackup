CREATE TABLE IF NOT EXISTS schemas(
    id bigint,
    schema_definition text,
    subject text,
    version bigint,
    deleted boolean,
    created timestamp,
    updated timestamp,
    PRIMARY KEY (id, subject, version)
);
