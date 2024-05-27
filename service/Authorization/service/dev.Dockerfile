# Use a base image with Java and Gradle installed
FROM gradle:7.3.3-jdk11 AS builder

# Set working directory
WORKDIR /app

# Clone the code from GitHub
RUN git clone https://github.com/your-username/your-repo.git .

# Build the application with Gradle
RUN gradle build

# Use a base image with Java installed
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
