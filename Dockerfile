# Use an official OpenJDK 17 runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the target directory to the container
COPY target/inventory_manager-0.0.1-SNAPSHOT.jar /app/inventory_manager.jar

# Expose the application's port
EXPOSE 8080

# Set environment variables for MySQL (you can customize these as needed)
# Use host.docker.internal to access the host's MySQL server
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-ctin4tggph6c7389u3p0-a/invenotry_management?\
    SPRING_DATASOURCE_USERNAME=i_user \
    SPRING_DATASOURCE_PASSWORD=SAM2vAjgD1OazngeVwRvzG9jF9BGOoGd

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/inventory_manager.jar"]
