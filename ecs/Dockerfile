FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package

# --------------------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
RUN apt-get update && apt-get install -y unzip curl && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && ./aws/install && rm -rf awscliv2.zip aws

COPY --from=builder /app /app
COPY --from=builder /root/.m2 /root/.m2

COPY ecs/bin/run-gatling.sh /app/run-gatling.sh
RUN chmod +x /app/run-gatling.sh

#ENTRYPOINT ["./mvnw", "gatling:test"]
ENTRYPOINT ["/app/run-gatling.sh"]