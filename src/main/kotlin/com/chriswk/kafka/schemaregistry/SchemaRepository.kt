package com.chriswk.kafka.schemaregistry

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.Instant
import java.util.Optional
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "schemas")
data class Schema(
    @Id val id: UUID = UUID.randomUUID(),
    val schemaId: Long,
    @Column(name = "schema_definition") val schema: String,
    val subject: String,
    val version: Long,
    val deleted: Boolean,
    @CreationTimestamp
    val created: Instant = Instant.now(),
    @UpdateTimestamp
    val updated: Instant = Instant.now()
)

interface SchemaRepository : PagingAndSortingRepository<Schema, UUID> {
    fun findAllBySchemaId(schemaId: Long): List<Schema>
    fun findAllBySchemaIdAndSubject(schemaId: Long, subject: String): List<Schema>
    fun findAllBySubject(subject: String): List<Schema>
    fun findBySubjectAndVersion(subject: String, version: Long): Schema?
    fun findBySchemaIdAndSubjectAndVersion(schemaId: Long, subject: String, version: Long): Schema?
}