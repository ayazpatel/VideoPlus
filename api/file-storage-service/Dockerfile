# Dockerfile for File Storage Service
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/file-storage-service-0.0.1-SNAPSHOT.jar file-storage-service.jar
EXPOSE 9003
ENTRYPOINT ["java", "-jar", "file-storage-service.jar"]
