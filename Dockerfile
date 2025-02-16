FROM openjdk:17-jdk

WORKDIR /app

COPY target/expense-tracker-0.0.1-SNAPSHOT.jar /app/expense-tracker.jar

ENTRYPOINT ["java", "-jar", "expense-tracker.jar"]