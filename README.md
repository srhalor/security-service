# security-service

Spring Boot Security service with Spring Security and Actuator support.

## Features

- Spring Boot 3.2.0
- Spring Security with HTTP Basic Authentication
- Spring Boot Actuator for health monitoring
- Custom health check API endpoint

## Requirements

- Java 17 or higher
- Maven 3.6+

## Building the Project

```bash
mvn clean package
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check Endpoints (Public)

- **GET** `/api/health` - Custom health check API that returns detailed health status from actuator
- **GET** `/actuator/health` - Built-in actuator health endpoint

### Example Response

```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 76887154688,
        "free": 16996110336,
        "threshold": 10485760,
        "path": "/path/to/app",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

## Security

- All endpoints except health checks require HTTP Basic Authentication
- Default security password is generated on startup (check console logs)
- For production use, configure proper authentication in `application.yml`

## Testing

```bash
mvn test
```
