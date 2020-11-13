CREATE INDEX "schema_subject_version_idx" ON schemas(subject, version);
CREATE INDEX "schema_id_idx" ON schemas(id);
CREATE INDEX "schema_id_subject" ON schemas(id, subject);