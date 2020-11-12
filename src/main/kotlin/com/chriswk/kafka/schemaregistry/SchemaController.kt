package com.chriswk.kafka.schemaregistry

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/schemas")
class SchemaController(val repo: SchemaRepository) {

    @RequestMapping("/ids")
    fun getAllIds(): List<Long> {
        return repo.findAll().map { it.schemaId }
    }
    @RequestMapping("/ids/{id}")
    fun getById(@PathVariable("id") id: Long): List<Schema> {
        return repo.findBySchemaId(id)
    }

    @RequestMapping("/ids/{id}/versions")
    fun getVersionsById(@PathVariable("id") id: Long): List<SubjectAndVersion> {
        return repo.findBySchemaId(id).map {
            SubjectAndVersion(it.subject, it.version)
        }
    }
}

data class SubjectAndVersion(val subject: String, val version: Long)