CREATE TABLE IF NOT EXISTS schemas(
    id bigint PRIMARY KEY,
    schema_definition text,
    subject text,
    version bigint,
    created timestamp,
    updated timestamp
);
CREATE INDEX "schema_subject_version" ON schemas(subject, version)