FROM openjdk:11
RUN mkdir /app
COPY /target/subte-microservice-1.0.0-SNAPSHOT.jar /app/subte.jar
WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "subte.jar"]
