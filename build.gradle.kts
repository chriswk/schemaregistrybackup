import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.4.10")
    kotlin("plugin.spring").version("1.4.10")
    kotlin("plugin.serialization").version("1.4.10")
    kotlin("plugin.jpa").version("1.4.10")
    id("org.springframework.boot").version("2.4.0")
    id("io.spring.dependency-management").version("1.0.10.RELEASE")
}

repositories {
    jcenter()
    maven(url = "http://packages.confluent.io/maven/")
}
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.kafka:spring-kafka")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:postgresql:1.15.0")
    testImplementation("org.testcontainers:kafka:1.15.0")
    testImplementation("org.testcontainers:junit-jupiter:1.15.0")
    testImplementation("io.confluent:kafka-avro-serializer:6.0.0")
}
tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
val dockerUsername: String by project
val dockerPassword: String by project

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    imageName = "docker.io/chriswk/schemaregistrybackup"
    isPublish = true
    docker {
        publishRegistry {
            username = dockerUsername
            password = dockerPassword
        }
    }
}