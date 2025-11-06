FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/order-execution-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9002

CMD ["java", "-jar", "app.jar"]