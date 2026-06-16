FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle gradle.properties .editorconfig ./
COPY domain domain
COPY application application
COPY persistence persistence
COPY web web
COPY lockConnector lockConnector
COPY bootstrap bootstrap
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/bootstrap/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
