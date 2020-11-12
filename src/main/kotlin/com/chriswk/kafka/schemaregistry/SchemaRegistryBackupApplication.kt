package com.chriswk.kafka.schemaregistry

import kotlinx.serialization.json.Json
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
@EnableJdbcRepositories
class SchemaRegistryBackupApplication {
    @Bean
    fun jsonSer(): Json {
        return Json.Default
    }
}

fun main(args: Array<String>) {
    runApplication<SchemaRegistryBackupApplication>(*args)
}