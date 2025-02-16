FROM maven:3.8-openjdk-17 AS build

WORKDIR /app

COPY pom.xml /app/
COPY src /app/src

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/target/expense-tracker-0.0.1-SNAPSHOT.jar /app/expense-tracker.jar

ENTRYPOINT ["java", "-jar", "expense-tracker.jar"]