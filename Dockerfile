FROM openjdk:21-jdk

WORKDIR /app

COPY /build/libs/order-execution-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9002

CMD ["java", "-jar", "app.jar"]