package com.chriswk.kafka.schemaregistry

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
@RequestMapping("/schemas")
class SchemaController(val repo: SchemaRepository) {

    @RequestMapping("/ids")
    fun getAllIds(): List<Long> {
        return repo.findAll().map { it.schemaId.id }
    }

    @RequestMapping("/ids/{id}")
    fun getById(@PathVariable("id") id: Long): String {
        return repo.findAllBySchemaIdId(id).firstOrNull()?.let {
            it.schema
        } ?: throw SchemaNotFoundException("Could not find schema with id: $id")
    }

    @RequestMapping("/ids/{id}/versions")
    fun getVersionsById(@PathVariable("id") id: Long): List<SubjectAndVersion> {
        return repo.findAllBySchemaIdId(id).map {
            SubjectAndVersion(it.schemaId.subject, it.schemaId.version)
        }
    }
}

data class SubjectAndVersion(val subject: String, val version: Long)
class SchemaNotFoundException(override val message: String) : Exception(message)