FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /work

COPY pom.xml /work/
RUN mvn dependency:resolve
RUN mvn verify clean --fail-never

COPY src /work/src/
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim


WORKDIR /app

COPY --from=build /work/target/*.jar /app/work.jar

EXPOSE 2751

ENTRYPOINT ["java", "-jar", "work.jar"]