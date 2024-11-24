FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /auth

COPY pom.xml /auth/
RUN mvn dependency:resolve
RUN mvn verify clean --fail-never

COPY src /auth/src/
RUN mvn clean package -DskipTests

COPY tables/table.sql /docker-entrypoint-initdb.d/init.sql

FROM openjdk:21-jdk-slim


WORKDIR /app

COPY --from=build /auth/target/*.jar /app/auth.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "auth.jar"]