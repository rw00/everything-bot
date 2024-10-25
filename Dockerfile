FROM maven:3.9.9-eclipse-temurin-21 AS builder
RUN mkdir /app-files
WORKDIR /app-files
COPY . /app-files
RUN chmod +x mvnw
RUN ./mvnw clean verify

FROM eclipse-temurin:21-jre-alpine
COPY --from=builder /app-files/target/everything-bot-*.jar everything-bot-1.0.0.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=live", "/everything-bot-1.0.0.jar"]
