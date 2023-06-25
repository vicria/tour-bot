FROM maven:3.8.1-openjdk-11 AS build
ENV HOME=/tour-bot
RUN mkdir -p $HOME
WORKDIR $HOME

COPY ../../. $HOME/.

RUN cd ../../
RUN mvn clean install -DskipTests

FROM openjdk:11
RUN mkdir /app
COPY --from=build /tour-bot/telegram/telegram-microservice/target/telegram-microservice-1.0.0-SNAPSHOT.jar /app/telegram.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "telegram.jar"]
