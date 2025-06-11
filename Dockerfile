# Stage 1: Build the JAR file
FROM gradle:8.8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: Create the final image
FROM amazoncorretto:17-alpine
WORKDIR /application

# Expose port
EXPOSE 8080