FROM eclipse-temurin:21-jre-alpine
RUN mkdir /app
COPY /target/subte-microservice-1.2.0-SNAPSHOT.jar /app/subte.jar
WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "subte.jar"]