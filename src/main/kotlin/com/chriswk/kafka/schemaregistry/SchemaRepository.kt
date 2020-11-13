package com.chriswk.kafka.schemaregistry

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.io.Serializable
import java.time.Instant
import java.util.Optional
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Embeddable
data class SchemaId(val id: Long, val subject: String, val version: Long) : Serializable

@Entity
@Table(name = "schemas")
data class Schema(
    @EmbeddedId
    val schemaId: SchemaId,
    @Column(name = "schema_definition") val schema: String,
    val deleted: Boolean,
    @CreationTimestamp
    val created: Instant = Instant.now(),
    @UpdateTimestamp
    val updated: Instant = Instant.now()
)

interface SchemaRepository : PagingAndSortingRepository<Schema, SchemaId> {
    fun findAllBySchemaIdId(id: Long): List<Schema>
    fun findAllBySchemaIdIdAndSchemaIdSubject(id: Long, subject: String): List<Schema>
    fun findAllBySchemaIdSubject(subject: String): List<Schema>
    fun findBySchemaIdSubjectAndSchemaIdVersion(subject: String, version: Long): Schema?
}