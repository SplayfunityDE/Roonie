FROM amazoncorretto:21

RUN mkdir /bot
COPY media /bot/media
COPY roonie-1.0-SNAPSHOT.jar /bot

ENTRYPOINT ["java", "-jar", "/bot/roonie-1.0-SNAPSHOT.jar"]
