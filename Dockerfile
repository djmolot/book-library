# Use an official Amazon Corretto JDK 17 runtime as a parent image
FROM amazoncorretto:17

# Set the JAR file argument
ARG JAR_FILE=target/*.jar

# Create a directory for the application
WORKDIR /app

# Copy your application's JAR file into the container
COPY ${JAR_FILE} /app/app.jar

# Copy configuration files if needed
COPY src/main/resources/application.yml /app/application.yml

# Command to run your application
ENTRYPOINT ["java","-jar","/app/app.jar"]
