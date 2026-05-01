FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-25-jdk-headless

WORKDIR /bot
COPY media /bot/media
COPY build/libs/roonie-1.0.0.jar /bot/app.jar

ENTRYPOINT ["java", "-jar", "/bot/app.jar"]
