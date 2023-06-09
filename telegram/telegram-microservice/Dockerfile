FROM openjdk:11
RUN mkdir /app
COPY /target/telegram-microservice-1.0.0-SNAPSHOT.jar /app/telegram.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "telegram.jar"]
