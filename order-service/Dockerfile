# Base image with Java 21
FROM eclipse-temurin:21-jre

# Create app directory
WORKDIR /app

# Copy built jar into container
COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Order Service)
EXPOSE 8081

# Run Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
