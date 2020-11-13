package com.chriswk.kafka.schemaregistry

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.LogManager
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class SchemaConsumer(val json: Json, val schemaRepository: SchemaRepository) {

    @KafkaListener(groupId = "schema-registry-backup", topics = ["_schemas"])
    fun saveSchema(message: ConsumerRecord<String, String>) {
        val key = json.decodeFromString(SchemaKey.serializer(), message.key())
        if (key.keytype == "SCHEMA") {
            val schema = json.decodeFromString(SchemaMessage.serializer(), message.value()).toSchemaEntity()
            schemaRepository.save(schema)
            logger.info("Saved $schema")
        } else {
            logger.info("Unknown keytype")
        }
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}

@Serializable
data class SchemaKey(val keytype: String, val subject: String, val version: Long, val magic: Long)
@Serializable
data class SchemaMessage(
    val subject: String,
    val version: Long,
    val id: Long,
    val schema: String,
    val deleted: Boolean
) {
    fun toSchemaEntity(): Schema {
        return Schema(
            schemaId = SchemaId(id = id,
            subject = subject,
            version = version),
            schema = schema,
            deleted = deleted
        )
    }
}