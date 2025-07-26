# VideoPlus - Microservices Architecture

## Overview
VideoPlus is a microservices-based application built with Spring Boot 3.5.3. The architecture uses minimal dependencies and manual implementations for better control and maintainability.

## Architecture
The application consists of 5 main services:

### 1. **Discovery Server** (Port: 8761)
- **Purpose**: Service registry where all microservices register themselves
- **Technology**: Eureka Server
- **Function**: Helps services find and communicate with each other

### 2. **API Gateway** (Port: 8080)
- **Purpose**: Single entry point for all client requests
- **Technology**: Spring Web
- **Function**: Routes requests to appropriate microservices

### 3. **Auth Service** (Port: 9001)
- **Purpose**: Handles user authentication and authorization
- **Database**: MongoDB
- **Functions**: 
  - User registration
  - User login
  - User logout
  - Password management

### 4. **User Service** (Port: 9002)
- **Purpose**: Manages user profiles and information
- **Database**: MongoDB
- **Functions**:
  - Get user profile
  - Update user information
  - Delete user account
  - Manage user history

### 5. **File Storage Service** (Port: 9003)
- **Purpose**: Handles file upload and storage operations
- **Storage**: MinIO (S3-compatible object storage)
- **Functions**:
  - Upload user avatar
  - Upload cover images
  - Delete files
  - Manage file metadata

## Dependencies Used (Minimal Set)
- **Spring Boot Starter Web**: For REST API endpoints
- **Spring Boot Starter Data MongoDB**: For database operations
- **Spring Cloud Netflix Eureka**: For service discovery
- **MinIO Client**: For file storage operations

## Key Features

### ✅ **Clean Architecture**
- **No Lombok**: All getters/setters are manually implemented
- **Minimal Dependencies**: Only essential Spring components
- **Clean Code**: Well-structured and documented methods
- **Traditional Spring Web**: Uses blocking I/O for reliability

### ✅ **Professional Implementation**
- Manual getter/setter methods for clarity
- Direct authentication implementation
- RESTful API design following best practices
- Clear separation of concerns

### ✅ **Well-Documented Code**
Every method includes proper documentation:
- Method functionality
- Parameter descriptions
- Return value information
- Business logic context

## Project Structure
```
VideoPlus/
├── api/
│   ├── discovery-server/          # Eureka Server
│   ├── api-gateway/              # Request routing
│   ├── auth-service/             # Authentication
│   ├── user-service/             # User management
│   └── file-storage-service/     # File operations
└── infra/                        # Docker configuration
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB running on localhost:27017
- MinIO server running on localhost:9000

### Step-by-Step Startup

1. **Start Discovery Server first**
   ```bash
   cd api/discovery-server
   mvn spring-boot:run
   ```
   Wait for it to fully start (check http://localhost:8761)

2. **Start other services in any order**
   ```bash
   # Terminal 2 - Auth Service
   cd api/auth-service
   mvn spring-boot:run
   
   # Terminal 3 - User Service  
   cd api/user-service
   mvn spring-boot:run
   
   # Terminal 4 - File Storage Service
   cd api/file-storage-service
   mvn spring-boot:run
   
   # Terminal 5 - API Gateway
   cd api/api-gateway
   mvn spring-boot:run
   ```

### Verify Services
- Discovery Server: http://localhost:8761
- API Gateway: http://localhost:8080/health
- Auth Service: http://localhost:9001
- User Service: http://localhost:9002
- File Storage Service: http://localhost:9003

## API Endpoints

### Authentication Endpoints (via Gateway)
```
POST /api/auth/register  - Register new user
POST /api/auth/login     - Login user
POST /api/auth/logout    - Logout user
```

### User Management Endpoints (via Gateway)
```
GET    /api/users           - Get all users
GET    /api/users/{id}      - Get user by ID
GET    /api/users/username/{username} - Get user by username
POST   /api/users           - Create user
PUT    /api/users/{id}      - Update user
DELETE /api/users/{id}      - Delete user
```

### File Storage Endpoints (via Gateway)
```
POST   /api/files/upload    - Upload file
DELETE /api/files/delete    - Delete file
```

## Technical Overview

This implementation demonstrates:

1. **Microservices Architecture**: Distributed services communicating via REST APIs
2. **Service Discovery**: Service registration and discovery using Eureka
3. **API Gateway Pattern**: Centralized request routing and load balancing
4. **REST API Design**: RESTful endpoints with proper HTTP methods
5. **Database Operations**: CRUD operations with MongoDB
6. **File Storage**: Object storage integration with MinIO
7. **Spring Boot**: Modern Java framework with auto-configuration

## Configuration Files

Each service has its own `application.properties` with:
- Server port configuration
- Database connection settings
- Eureka client configuration
- Service-specific properties

## Code Quality Features

1. **Clear Method Names**: Descriptive and meaningful method naming
2. **Comprehensive Documentation**: Well-documented classes and methods
3. **Error Handling**: Robust exception handling with meaningful messages
4. **Consistent Response Format**: Standardized JSON response structure
5. **Separation of Concerns**: Single responsibility principle applied

## Future Enhancements

Potential improvements and extensions:
1. JWT-based authentication and authorization
2. Enhanced security with Spring Security
3. Reactive programming with WebFlux
4. Database transactions and data consistency
5. Caching layer with Redis
6. API documentation with OpenAPI/Swagger
7. Monitoring and logging infrastructure
8. Comprehensive test coverage

This architecture provides a solid foundation for scalable microservices development.


