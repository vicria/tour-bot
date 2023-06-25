FROM maven:3.8.1-openjdk-11 AS build
ENV HOME=/tour-bot
RUN mkdir -p $HOME
WORKDIR $HOME

COPY ../../. $HOME/.

RUN cd ../../
RUN mvn clean install -DskipTests

FROM openjdk:11
RUN mkdir /app
COPY --from=build /tour-bot/subte/subte-microservice/target/subte-microservice-1.0.0-SNAPSHOT.jar /app/subte.jar
WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "subte.jar"]
