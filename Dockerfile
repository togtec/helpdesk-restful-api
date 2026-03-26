# Use a lightweight Java runtime
FROM eclipse-temurin:17-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy the jar file into the container
COPY target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]