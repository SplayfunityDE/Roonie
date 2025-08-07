FROM amazoncorretto:21

RUN mkdir /bot
COPY media /bot/media
COPY /home/runner/work/Roonie/Roonie/build/libs/roonie-1.0-SNAPSHOT.jar /bot

ENTRYPOINT ["java", "-jar", "/bot/roonie-1.0-SNAPSHOT.jar"]
