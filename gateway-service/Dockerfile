FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Build context is the repo root (see docker-compose.yml), so the full
# multi-module reactor (parent pom.xml + every module folder) is available -
# required because the parent pom's <modules> list references all of them.
COPY . .
RUN mvn -q -pl gateway-service -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/gateway-service/target/gateway-service.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
