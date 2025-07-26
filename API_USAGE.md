# VideoPlus API Usage Guide

## Architecture Overview

VideoPlus is a Spring Boot microservices-based application designed for video management and storage. The architecture follows a simple, beginner-friendly approach with minimal dependencies.

### Service Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Frontend      │────│   API Gateway    │────│ Discovery Server│
│   (Port: N/A)   │    │   (Port: 8080)   │    │  (Port: 8761)   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                              │
                              ├─────────────────────────────┐
                              │                             │
                       ┌──────▼──────┐              ┌──────▼──────┐
                       │ Auth Service│              │User Service │
                       │(Port: 8081) │              │(Port: 8082) │
                       └─────────────┘              └─────────────┘
                              │
                       ┌──────▼──────┐
                       │File Storage │
                       │ Service     │
                       │(Port: 8083) │
                       └─────────────┘
                              │
                       ┌──────▼──────┐
                       │   MinIO     │
                       │ Object      │
                       │ Storage     │
                       └─────────────┘
```

### Core Services

#### 1. Discovery Server (Eureka)
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Technology**: Spring Cloud Netflix Eureka
- **Dependencies**: Only Eureka Server

#### 2. API Gateway
- **Port**: 8080
- **Purpose**: Single entry point for all API requests
- **Technology**: Spring Web with RestTemplate
- **Features**: Simple request routing and proxying
- **Dependencies**: Spring Web, Eureka Client

#### 3. Auth Service
- **Port**: 8081
- **Purpose**: User authentication and authorization
- **Technology**: Spring Web, MongoDB
- **Features**: User registration, login, profile management
- **Dependencies**: Spring Web, Spring Data MongoDB, Eureka Client

#### 4. User Service
- **Port**: 8082
- **Purpose**: User profile and data management
- **Technology**: Spring Web, MongoDB
- **Features**: User CRUD operations, profile updates
- **Dependencies**: Spring Web, Spring Data MongoDB, Eureka Client

#### 5. File Storage Service
- **Port**: 8083
- **Purpose**: File upload, download, and management
- **Technology**: Spring Web, MinIO
- **Features**: Video/file upload, download, deletion
- **Dependencies**: Spring Web, MinIO Client, Eureka Client

## Database Architecture

- **MongoDB**: Document database for user data, authentication, and metadata
- **MinIO**: Object storage for files and videos

## API Endpoints Guide

### Discovery Server APIs

**Base URL**: `http://localhost:8761`

#### Service Registry Dashboard
```http
GET /
```
**Description**: Eureka dashboard to view registered services

**Response**: HTML dashboard showing all registered services

---

### API Gateway Routes

**Base URL**: `http://localhost:8080`

All requests are proxied through the API Gateway to respective services.

#### Auth Service Routes (via Gateway)
```http
POST /auth/register
POST /auth/login
GET /auth/profile/{userId}
PUT /auth/profile/{userId}
DELETE /auth/user/{userId}
GET /auth/users
```

#### User Service Routes (via Gateway)
```http
GET /user/{id}
POST /user
PUT /user/{id}
DELETE /user/{id}
GET /user/all
```

#### File Storage Routes (via Gateway)
```http
POST /files/upload
GET /files/download/{filename}
DELETE /files/delete/{filename}
GET /files/list
```

---

### Auth Service APIs

**Base URL**: `http://localhost:8081` (Direct) or `http://localhost:8080/auth` (via Gateway)

#### 1. User Registration
```http
POST /auth/register
```

**Request Body**:
```json
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123"
}
```

**Response** (Success - 201):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "username": "john_doe",
    "email": "john@example.com",
    "createdAt": "2024-01-15T10:30:00Z"
}
```

**Response** (Error - 400):
```json
{
    "error": "User already exists with this email"
}
```

#### 2. User Login
```http
POST /auth/login
```

**Request Body**:
```json
{
    "email": "john@example.com",
    "password": "securePassword123"
}
```

**Response** (Success - 200):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "username": "john_doe",
    "email": "john@example.com",
    "message": "Login successful"
}
```

**Response** (Error - 401):
```json
{
    "error": "Invalid credentials"
}
```

#### 3. Get User Profile
```http
GET /auth/profile/{userId}
```

**Response** (Success - 200):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "username": "john_doe",
    "email": "john@example.com",
    "createdAt": "2024-01-15T10:30:00Z"
}
```

#### 4. Update User Profile
```http
PUT /auth/profile/{userId}
```

**Request Body**:
```json
{
    "username": "john_doe_updated",
    "email": "john.updated@example.com"
}
```

**Response** (Success - 200):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "username": "john_doe_updated",
    "email": "john.updated@example.com",
    "updatedAt": "2024-01-15T11:30:00Z"
}
```

#### 5. Delete User
```http
DELETE /auth/user/{userId}
```

**Response** (Success - 200):
```json
{
    "message": "User deleted successfully"
}
```

#### 6. Get All Users
```http
GET /auth/users
```

**Response** (Success - 200):
```json
[
    {
        "id": "64f5a7b2c1d2e3f4a5b6c7d8",
        "username": "john_doe",
        "email": "john@example.com",
        "createdAt": "2024-01-15T10:30:00Z"
    },
    {
        "id": "64f5a7b2c1d2e3f4a5b6c7d9",
        "username": "jane_doe",
        "email": "jane@example.com",
        "createdAt": "2024-01-15T09:30:00Z"
    }
]
```

---

### User Service APIs

**Base URL**: `http://localhost:8082` (Direct) or `http://localhost:8080/user` (via Gateway)

#### 1. Get User by ID
```http
GET /user/{id}
```

**Response** (Success - 200):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "name": "John Doe",
    "email": "john@example.com",
    "age": 25,
    "city": "New York"
}
```

#### 2. Create New User
```http
POST /user
```

**Request Body**:
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "age": 25,
    "city": "New York"
}
```

**Response** (Success - 201):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "name": "John Doe",
    "email": "john@example.com",
    "age": 25,
    "city": "New York"
}
```

#### 3. Update User
```http
PUT /user/{id}
```

**Request Body**:
```json
{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "age": 26,
    "city": "Boston"
}
```

**Response** (Success - 200):
```json
{
    "id": "64f5a7b2c1d2e3f4a5b6c7d8",
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "age": 26,
    "city": "Boston"
}
```

#### 4. Delete User
```http
DELETE /user/{id}
```

**Response** (Success - 200):
```json
{
    "message": "User deleted successfully"
}
```

#### 5. Get All Users
```http
GET /user/all
```

**Response** (Success - 200):
```json
[
    {
        "id": "64f5a7b2c1d2e3f4a5b6c7d8",
        "name": "John Doe",
        "email": "john@example.com",
        "age": 25,
        "city": "New York"
    },
    {
        "id": "64f5a7b2c1d2e3f4a5b6c7d9",
        "name": "Jane Doe",
        "email": "jane@example.com",
        "age": 23,
        "city": "Los Angeles"
    }
]
```

---

### File Storage Service APIs

**Base URL**: `http://localhost:8083` (Direct) or `http://localhost:8080/files` (via Gateway)

#### 1. Upload File
```http
POST /files/upload
```

**Request**: Multipart form data
- `file`: The file to upload (video, image, document)

**Example using curl**:
```bash
curl -X POST -F "file=@video.mp4" http://localhost:8083/files/upload
```

**Response** (Success - 200):
```json
{
    "message": "File uploaded successfully",
    "filename": "video.mp4",
    "size": 1024000,
    "contentType": "video/mp4"
}
```

#### 2. Download File
```http
GET /files/download/{filename}
```

**Example**:
```http
GET /files/download/video.mp4
```

**Response**: Binary file data with appropriate headers

#### 3. Delete File
```http
DELETE /files/delete/{filename}
```

**Response** (Success - 200):
```json
{
    "message": "File deleted successfully",
    "filename": "video.mp4"
}
```

#### 4. List All Files
```http
GET /files/list
```

**Response** (Success - 200):
```json
[
    {
        "filename": "video.mp4",
        "size": 1024000,
        "lastModified": "2024-01-15T10:30:00Z"
    },
    {
        "filename": "image.jpg",
        "size": 256000,
        "lastModified": "2024-01-15T09:15:00Z"
    }
]
```

---

## Service Startup Guide

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB running on localhost:27017
- MinIO running on localhost:9000

### Startup Order

1. **Start Discovery Server** (Port 8761)
```bash
cd api/discovery-server
mvn spring-boot:run
```

2. **Start Auth Service** (Port 8081)
```bash
cd api/auth-service
mvn spring-boot:run
```

3. **Start User Service** (Port 8082)
```bash
cd api/user-service
mvn spring-boot:run
```

4. **Start File Storage Service** (Port 8083)
```bash
cd api/file-storage-service
mvn spring-boot:run
```

5. **Start API Gateway** (Port 8080)
```bash
cd api/api-gateway
mvn spring-boot:run
```

### Verification

1. Check Eureka Dashboard: `http://localhost:8761`
2. Test API Gateway: `http://localhost:8080/auth/users`
3. Test direct service: `http://localhost:8081/auth/users`

## Configuration Details

### Application Properties

Each service has minimal configuration:

#### Discovery Server
```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

#### API Gateway
```properties
server.port=8080
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

#### Auth Service
```properties
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/videoplus
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

#### User Service
```properties
server.port=8082
spring.data.mongodb.uri=mongodb://localhost:27017/videoplus
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

#### File Storage Service
```properties
server.port=8083
minio.endpoint=http://localhost:9000
minio.accessKey=minioadmin
minio.secretKey=minioadmin
minio.bucketName=videoplus-files
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

## Error Handling

All services return consistent error responses:

### Common Error Formats

**400 Bad Request**:
```json
{
    "error": "Invalid request data",
    "details": "Username is required"
}
```

**404 Not Found**:
```json
{
    "error": "Resource not found",
    "details": "User with ID 123 not found"
}
```

**500 Internal Server Error**:
```json
{
    "error": "Internal server error",
    "details": "Database connection failed"
}
```

## Development Notes

### Code Style
- No Lombok - all getters/setters are manually written
- Simple, blocking code - no reactive programming
- Minimal dependencies - only essential Spring components
- Beginner-friendly - well-commented and straightforward

### Database Schema
- MongoDB collections: `users`, `auth_users`
- MinIO bucket: `videoplus-files`

### Testing
Use tools like Postman, curl, or any REST client to test the APIs.

### Future Enhancements
- Add pagination to list endpoints
- Implement proper authentication/authorization
- Add input validation
- Add logging and monitoring
- Add unit and integration tests
