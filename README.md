# Schema registry backup to JDBC compatible database
* Reads _schemas topic, writes to database

# Running locally
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

## Authenticated kafka connection
Since this is using Spring kafka, overriding spring's properties will also be supported
## Queries
Will support the GET calls defined in Confluence's api doc for schema-registry
https://docs.confluent.io/current/schema-registry/develop/api.html