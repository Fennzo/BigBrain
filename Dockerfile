# Specify the base image
FROM maven:3.9.1-amazoncorretto-19

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container
COPY . /app

# Install Maven and other dependencies
RUN mvn clean install
# Expose the port that the application listens on
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/SpringMVC-0.0.1-SNAPSHOT.jar"]
