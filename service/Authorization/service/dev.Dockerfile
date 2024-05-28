# Use a base image with Java and Gradle installed
FROM gradle:7.5-jdk17 AS builder

# Set working directory
WORKDIR /app

# Clone the code from GitHub
RUN git clone https://github.com/Tunaxx-New/awesome-ecommerce-project.git /spring-dev

WORKDIR /spring-dev/service/Authorization/service/auth

# Build the application with Gradle
RUN gradle build -x test --stacktrace

# Use a base image with Java installed
FROM openjdk:17-jdk-slim

# Copy the built JAR file from the builder stage
COPY --from=builder /spring-dev/service/Authorization/service/auth/build/libs/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
