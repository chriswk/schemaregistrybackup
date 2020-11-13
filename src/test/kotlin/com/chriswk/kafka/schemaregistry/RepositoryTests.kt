package com.chriswk.kafka.schemaregistry

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.temporal.ChronoUnit

@DataJpaTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RepositoryTests {
    @Autowired
    lateinit var schemaRepository: SchemaRepository

    companion object {
        @Container
        val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:13").apply {
            withUsername("schemaregistrybackup")
            withDatabaseName("schemaregistrybackup")
            withPassword("schemaregistrybackup")
        }
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }


    @Test
    fun ableToSave() {
        val schema = schemaRepository.save(
            Schema(
                SchemaId(id = 1L,subject = "test-topic-value", version = 1), schema = """
            {"namespace": "example.avro",
             "type": "record",
             "name": "User",
             "fields": [
                 {"name": "name", "type": "string"},
                 {"name": "favorite_number",  "type": ["int", "null"]},
                 {"name": "favorite_color", "type": ["string", "null"]}
             ]
            }
        """.trimIndent(), deleted = false
            )
        )
        val fetchedSchema = schemaRepository.findById(SchemaId(1L, "test-topic-value", 1))
        assertThat(fetchedSchema).hasValueSatisfying {
            assertThat(it.schema).isEqualTo(schema.schema)
            assertThat(it.schemaId).isEqualTo(schema.schemaId)
            assertThat(it.created).isCloseTo(schema.created, within(1, ChronoUnit.SECONDS))
            assertThat(it.updated).isCloseTo(schema.updated, within(1, ChronoUnit.SECONDS))
        }
    }
}

