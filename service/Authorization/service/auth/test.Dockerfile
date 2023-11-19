# Use the official Gradle image as the base image
FROM gradle:8.4.0-jdk17-jammy

# Set the working directory
WORKDIR /app

# Copy only the necessary files for building
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

# Build the application using Gradle
RUN gradle clean build -x test --warning-mode=all --stacktrace

COPY . .

# Expose a port if your application needs it
EXPOSE 8079

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=test

# Entry point to run the application
ENTRYPOINT ["gradle", "test"]
