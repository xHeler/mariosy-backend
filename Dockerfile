# Use maven image for build
FROM maven:3.8-jdk-11 AS build

WORKDIR /app

# Copy the POM and the source code into the container
COPY pom.xml .
COPY src ./src

# Package the application without running tests
RUN mvn clean package -DskipTests

# Use Amazon Corretto image for runtime
FROM amazoncorretto:11

WORKDIR /app

# Copy the built jar file from the previous stage into this container
COPY --from=build /app/target/mariosy-backend-1.0-SNAPSHOT.jar /app/mariosy-backend.jar

# Copy the global-bundle.pem into the same directory
COPY global-bundle.pem /app/global-bundle.pem

# Run the application
CMD ["java", "-jar", "mariosy-backend.jar"]
