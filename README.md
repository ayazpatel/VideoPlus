# VideoPlus - Microservices Architecture

## Overview
VideoPlus is a microservices-based application built with Spring Boot 3.5.3. with minimal dependencies and manual implementations instead of using complex libraries.

## Architecture
The application consists of 5 main services:

### 1. **Discovery Server** (Port: 8761)
- **Purpose**: Service registry where all microservices register themselves
- **Technology**: Eureka Server
- **Function**: Helps services find and communicate with each other

### 2. **API Gateway** (Port: 8080)
- **Purpose**: Single entry point for all client requests
- **Technology**: Spring Web (simplified from Spring Cloud Gateway)
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

## Key Features of This Refactored Version

### ✅ **Beginner-Friendly Approach**
- **No Lombok**: All getters/setters are manually written with clear comments
- **No Complex Libraries**: Removed WebFlux, Spring Security, JWT libraries
- **Simple Code**: Each method has comments explaining what it does
- **Traditional Spring Web**: Uses blocking I/O instead of reactive programming

### ✅ **Manual Implementations**
- Manual getter/setter methods instead of Lombok annotations
- Simple authentication without JWT complexity
- Basic REST controllers without advanced features
- Clear separation of concerns

### ✅ **Educational Comments**
Every method includes comments explaining:
- What the method does
- What parameters it takes
- What it returns
- Why it's needed

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

## Learning Objectives

This refactored version helps beginners understand:

1. **Microservices Architecture**: How services communicate with each other
2. **Service Discovery**: How services find each other using Eureka
3. **API Gateway Pattern**: Single entry point for client requests
4. **REST API Design**: Proper HTTP methods and status codes
5. **Database Operations**: Basic CRUD operations with MongoDB
6. **File Storage**: Object storage with MinIO
7. **Spring Boot Basics**: Configuration, beans, dependency injection

## Configuration Files

Each service has its own `application.properties` with:
- Server port configuration
- Database connection settings
- Eureka client configuration
- Service-specific properties

## Code Quality Features

1. **Clear Method Names**: Each method name describes exactly what it does
2. **Comprehensive Comments**: Every class and method is documented
3. **Simple Error Handling**: Basic try-catch blocks with meaningful messages
4. **Consistent Response Format**: All APIs return consistent JSON responses
5. **Separation of Concerns**: Each service has a single responsibility

## Next Steps for Learning

After understanding this version, you can gradually add:
1. JWT-based authentication
2. Spring Security for better security
3. Reactive programming with WebFlux
4. Database transactions
5. Caching with Redis
6. API documentation with Swagger
7. Monitoring and logging
8. Unit and integration tests

This simplified version provides a solid foundation for understanding microservices architecture without overwhelming complexity!


