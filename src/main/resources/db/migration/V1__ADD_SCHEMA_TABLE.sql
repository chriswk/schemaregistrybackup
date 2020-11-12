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
CREATE INDEX "schema_subject_version_idx" ON schemas(subject, version);
CREATE INDEX "schema_id_idx" ON schemas(schema_id);
CREATE INDEX "schema_id_subject" ON schemas(schema_id, subject);