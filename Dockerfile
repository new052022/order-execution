FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/execution-order-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9002

CMD ["java", "-jar", "app.jar"]