FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package

# --------------------------------
FROM eclipse-temurin:25-jdk-jammy

WORKDIR /app
COPY --from=builder /app /app

ENTRYPOINT ["./mvnw", "gatling:test"]