package com.chriswk.kafka.schemaregistry

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class SchemaConsumer(val schemaRepository: SchemaRepository) {

    @KafkaListener(groupId = "schema-registry-backup", topics = ["__schemas"])
    fun saveSchema(message: ConsumerRecord<String, String>) {
        println(message.value())
    }
}