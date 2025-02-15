FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jre-slim

WORKDIR /app

COPY --from=build /app/target/expense-tracker-0.0.1-SNAPSHOT.jar /app/expense-tracker.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "expense-tracker.jar"]