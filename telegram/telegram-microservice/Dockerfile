FROM eclipse-temurin:21-jre-alpine
RUN mkdir /app
COPY /target/telegram-microservice-1.2.0-SNAPSHOT.jar /app/telegram.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "telegram.jar"]