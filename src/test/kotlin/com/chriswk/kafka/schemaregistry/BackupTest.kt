package com.chriswk.kafka.schemaregistry

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.SchemaBuilder
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@SpringBootTest
class BackupTest {
    companion object {
        val kafkaNetwork = Network.newNetwork()

        @Container
        val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:13").apply {
            withUsername("schemaregistrybackup")
            withDatabaseName("schemaregistrybackup")
            withPassword("schemaregistrybackup")
            withNetwork(kafkaNetwork)
        }

        @Container
        val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.0.0")).apply {
            withNetwork(kafkaNetwork)
            withNetworkAliases("kafka")
        }

        @Container
        val schemaRegistryContainer =
            GenericContainer<Nothing>(DockerImageName.parse("confluentinc/cp-schema-registry:6.0.0")).apply {
                withNetwork(kafkaNetwork)
                dependsOn(kafkaContainer)
                withNetworkAliases("schema-registry")
                withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                withEnv("SCHEMA_REGISTRY_KAFKASTORE_TOPIC_REPLICATION_FACTOR", "1")
                withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "kafka:9092")
                withStartupTimeout(Duration.ofMinutes(1))
                withExposedPorts(8081)
            }


        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
            registry.add("spring.kafka.producer.properties.schema.registry.url") {
                "http://${schemaRegistryContainer.host}:${
                    schemaRegistryContainer.getMappedPort(
                        8081
                    )
                }"
            }
            registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers)
        }

    }


    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, GenericRecord>

    @Autowired
    lateinit var schemaRepository: SchemaRepository

    @Test
    fun `schema submitted to schema registry should be reflected in backup`() {
        val schema = SchemaBuilder.builder("example.avro")
            .record("User").fields()
            .requiredString("name")
            .optionalInt("favorite_number")
            .optionalString("favorite_color")
            .endRecord()
        val record = GenericData.Record(schema)
        record.put("name", "Willy")
        record.put("favorite_number", 51)
        record.put("favorite_color", "Blue")
        kafkaTemplate.send(ProducerRecord("test-topic", "testkey", record)).get()
        val subjects = schemaRepository.findAllBySchemaIdSubject("test-topic-value")
        assertThat(subjects).isNotEmpty
        val parsedSchema = org.apache.avro.Schema.Parser().parse(subjects.first().schema)
        assertDoesNotThrow {
            val record2 = GenericData.Record(parsedSchema)
            record2.put("name", "Willy")
            record2.put("favorite_number", 51)
            record2.put("favorite_color", "Blue")
        }
    }

    @Test
    fun `Update to schema should also be reflected in backup`() {
        val schema = SchemaBuilder.builder("example.avro")
            .record("User").fields()
            .requiredString("name")
            .optionalInt("favorite_number")
            .optionalString("favorite_color")
            .endRecord()

        val record = GenericData.Record(schema)
        record.put("name", "Willy")
        record.put("favorite_number", 51)
        record.put("favorite_color", "Blue")
        kafkaTemplate.send(ProducerRecord("test-topic", "testkey", record)).get()
        val updatedSchema = """
                        {"namespace": "example.avro",
             "type": "record",
             "name": "User",
             "fields": [
                 {"name": "name", "type": "string"},
                 {"name": "favorite_number",  "type": ["int", "null"]},
                 {"name": "favorite_color", "type": ["string", "null"]},
                 {"name": "favorite_movie", "type": ["string", "null"], "default": "null"}
             ]
            }
        """.trimIndent()
        val parser = org.apache.avro.Schema.Parser()
        val updated = parser.parse(updatedSchema)
        val record2 = GenericData.Record(updated)
        record2.put("name", "Willy")
        record2.put("favorite_number", 51)
        record2.put("favorite_color", "Blue")
        record2.put("favorite_movie", "The Big Lebowski")
        kafkaTemplate.send(ProducerRecord("test-topic", "testkey2", record2)).get()
        val schemasForSubject = schemaRepository.findAllBySchemaIdSubject("test-topic-value")
        assertThat(schemasForSubject).hasSize(2)
    }

}