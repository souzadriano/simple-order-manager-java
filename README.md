# Simple Order Manager

## Prerequisites
- Java JDK 8
- Maven
- PostgreSQL
- E-Mail SMTP

### PostgreSQL in Container

Creating container

```
docker run --name som-postgres -e POSTGRES_USER=som -e POSTGRES_PASSWORD=som -p 5432:5432 -d postgres:14
```

Starting container

```
docker start som-postgres
```

## Start the application

```
./mvnw spring-boot:run
```

## Access Rest APIs Documentation and Try

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


## Test the application

```
./mvnw test
```

## Package the application

```
./mvnw package
```

## Run the packaged application with custom application.properties

Example:

```
java -jar target/simple-order-manager-1.0.0.jar --spring.config.location=/home/user/application.properties
```

**application.properties** example:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/som
spring.datasource.username=som
spring.datasource.password=som
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.validationQuery=SELECT 1

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.mail.host=localhost
spring.mail.port=587
spring.mail.username=USERNAME
spring.mail.password=PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

server.port=8080


```
