# Schema registry backup to JDBC compatible database
* Reads _schemas topic, writes to database

# Running
 
## Locally
You need to configure 4 environment variables
* KAFKA_BOOTSTRAP_SERVERS
    * Which should point to the kafka server you want to read schemas from
* JDBC_URL
    * The included build file adds the postgres jdbc driver
    * If you want to use a different database add a JDBC driver to `build.gradle.kts`"
    * Example postgres url would be `jdbc:postgresql://localhost:5432/schemaregistrybackup`
* JDBC_USERNAME
    * User to use to connect to database
* JDBC_PASSWORD
    * Password to connect to database

## Running in docker-compose
Starting docker-compose in the root directory of this project will start Zookeeper, Kafka, Schemaregistry, Schemaregistryui and this application.
```bash
$ docker-compose up -d
```
* Use the included schema-registry-ui to register schemas
* See that the subjects also become available under http://localhost:9900/subjects

## Authenticated kafka connection
Since this is using Spring kafka, overriding spring's properties will also be supported
## Queries
Will support the GET calls defined in Confluence's api doc for schema-registry
https://docs.confluent.io/current/schema-registry/develop/api.html