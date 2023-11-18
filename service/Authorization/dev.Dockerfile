FROM openjdk:17

#WORKDIR /app
#COPY target .

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=development

#ENTRYPOINT ["java", "-jar", "AuthApplication.jar"]
