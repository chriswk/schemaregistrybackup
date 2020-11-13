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
        return repo.findAll().map { it.schemaId }
    }

    @RequestMapping("/ids/{id}")
    fun getById(@PathVariable("id") id: Long): String {
        return repo.findAllBySchemaId(id).firstOrNull()?.let {
            it.schema
        } ?: throw SchemaNotFoundException("Could not find schema with id: $id")
    }

    @RequestMapping("/ids/{id}/versions")
    fun getVersionsById(@PathVariable("id") id: Long): List<SubjectAndVersion> {
        return repo.findAllBySchemaId(id).map {
            SubjectAndVersion(it.subject, it.version)
        }
    }
}

data class SubjectAndVersion(val subject: String, val version: Long)
class SchemaNotFoundException(override val message: String) : Exception(message)