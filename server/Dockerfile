# Use a base image with Java 17 and Gradle pre-installed
FROM gradle:7.2.0-jdk17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle .
COPY settings.gradle .
COPY gradle ./gradle


# Copy the Gradle Wrapper files
COPY gradlew .
COPY gradlew.bat .

# Ensure Gradle Wrapper files have executable permissions (Linux-based systems)
RUN chmod +x gradlew

# Copy the application source code
COPY src ./src

# Build the application
RUN ./gradlew clean build --no-daemon

FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Set environment variables for the database connection
ENV DB_HOST: dumbo.db.elephantsql.com
ENV DB_PORT: 5432
ENV DB_NAME: iypnmdkf
ENV DB_USERNAME: iypnmdkf
ENV DB_PASSWORD: KJOeDWINc6IajmfBGQt7SePXqZ51_IwD

# Expose the port that the application will run on
EXPOSE 8080

# Run the application when the container starts
CMD ["java", "-jar", "app.jar"]
