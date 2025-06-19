FROM amazoncorretto:21

RUN mkdir /bot
COPY radio-1.0-SNAPSHOT.jar /bot

ENTRYPOINT ["java", "-jar", "/bot/radio-1.0-SNAPSHOT.jar"]
