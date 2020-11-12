package com.chriswk.kafka.schemaregistry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
@EnableR2dbcRepositories
class SchemaRegistryBackupApplication

fun main(args: Array<String>) {
    runApplication<SchemaRegistryBackupApplication>(*args)
}