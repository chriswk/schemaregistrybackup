CREATE UNIQUE INDEX "schema_id_subject_version_uniq_idx" ON schemas(schema_id, subject, version);
CREATE INDEX "schema_subject_version_idx" ON schemas(subject, version);
CREATE INDEX "schema_id_idx" ON schemas(schema_id);
CREATE INDEX "schema_id_subject" ON schemas(schema_id, subject);