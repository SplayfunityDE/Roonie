FROM amazoncorretto:21

RUN mkdir /bot
COPY media /bot/media
COPY new-clyde-1.0-SNAPSHOT.jar /bot

ENTRYPOINT ["java", "-jar", "/bot/new-clyde-1.0-SNAPSHOT.jar"]