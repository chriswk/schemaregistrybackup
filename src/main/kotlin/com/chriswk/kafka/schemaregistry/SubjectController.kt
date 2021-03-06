package com.chriswk.kafka.schemaregistry

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subjects")
class SubjectController(val repo: SchemaRepository) {

    @GetMapping("/")
    fun allSubjects(): List<String> {
        return repo.findAll().map { it.schemaId.subject }
    }
    @GetMapping("/{subject}/versions")
    fun getSubjectVersions(@PathVariable("subject") subject: String): List<Long> {
        return repo.findAllBySchemaIdSubject(subject).map { it.schemaId.version }
    }
    @GetMapping("/{subject}/versions/{version}")
    fun getSubjectVersion(@PathVariable("subject") subject: String, @PathVariable("version") version: Long): Schema? {
        return repo.findBySchemaIdSubjectAndSchemaIdVersion(subject, version)
    }
}