# VideoTube - Spring Boot Microservice Project

This Spring Boot-based microservice project demonstrates production-grade microservices for large-scale applications, similar to YouTube. The project includes several completed services and a few under development.

## Completed Services
- **Eureka Service**: Service discovery
- **API Gateway**: Central request routing
- **Auth Service**: Authentication and authorization
- **User Service**: User data and profiles
- **File Storage Service**: File uploads and storage

## Incomplete Services
- **Video Service**
- **Media Service**
- **Apache Kafka Integration**

## Technology Stack
- **Spring Boot**
- **MinIO**
- **MongoDB**
- **Jakarta EE**
- **Java 17**
- **Spring Boot 3.5.3**

## Installation & Setup

### Prerequisites
- Java 17
- Spring Boot 3.5.3
- MongoDB

### Steps for Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/spring-boot-microservices.git
   ```

2. Run each service:

   - Start the Eureka Service.
   - Start the API Gateway.
   - Start the Auth Service.
   - Start the User Service.
   - Start the File Storage Service.

### Docker Compose Setup

Add the following `docker-compose.yml` to run all services in separate containers:

```yaml
version: '3'
services:
  eurekaserver:
    image: eurekaserver-image
    build: ./eureka
    ports:
      - "8761:8761"
  apigateway:
    image: apigateway-image
    build: ./api-gateway
    ports:
      - "8080:8080"
  authservice:
    image: authservice-image
    build: ./auth-service
    ports:
      - "8081:8081"
  userservice:
    image: userservice-image
    build: ./user-service
    ports:
      - "8082:8082"
  filestorage:
    image: filestorage-image
    build: ./file-storage
    ports:
      - "8083:8083"
```

Run services with Docker Compose:

```bash
docker-compose up
```

## Usage

- Start the Eureka Server (Port 8761).
- Start the API Gateway (Port 8080) to route requests.
- Interact with the API Gateway to access all services.

### Example API Endpoints

- `GET /auth/login` - Login
- `POST /user/register` - Create user
- `POST /file/upload` - Upload files

## License

This project is licensed under the MIT License. You are free to use, modify, and distribute the code, as long as you include the license and copyright notice in any copies of the software.