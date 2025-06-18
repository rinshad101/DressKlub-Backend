FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
