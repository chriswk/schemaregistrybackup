package com.chriswk.kafka.schemaregistry

import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository

data class Schema(@Id val id: Long, val schema: String, val subject: String, val version: Long)

interface SchemaRepository : ReactiveCrudRepository<Schema, Long>