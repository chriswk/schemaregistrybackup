CREATE TABLE IF NOT EXISTS schemas(
    id uuid PRIMARY KEY,
    schema_id bigint,
    schema_definition text,
    subject text,
    version bigint,
    deleted boolean,
    created timestamp,
    updated timestamp
);
