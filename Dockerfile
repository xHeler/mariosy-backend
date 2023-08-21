FROM maven:3.8-jdk-11 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:11

WORKDIR /app

COPY --from=build /app/target/mariosy-backend-1.0-SNAPSHOT.jar /app/mariosy-backend.jar

ENV SPRING_PROFILES_ACTIVE=local

CMD ["java", "-jar", "mariosy-backend.jar"]
