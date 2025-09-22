[Русская версия](README_RU.md)

# 🏦 Bank Cards System

Bank Cards System is a backend solution for card management, transfers, and user administration with REST API endpoints. The system supports role-based access (ADMIN/USER), CRUD operations on cards, transfer operations, and Redis caching.

---

## 🔍 Core Features

- Card and User management
- Role-based authentication (Admin/User)
- REST API endpoints for CMS panel functionality
- Transfer functionality between cards
- Redis caching for fast access
- DTOs for request objects

---

## ⚙️ Tech Stack

- Backend: Java 17+, Spring Boot, Spring Data JPA, MapStruct, Validation
- Database: PostgreSQL (main)
- Caching: Redis
- Security: Spring Security, JWT
- DevOps: Docker, Docker Compose
- Utilities: Lombok, Maven

---

## 🧩 Entities

- **User** — user management and authentication
- **Card** — card entity with balance, status, and owner
- **TransferRequest** — DTO for card transfer
- **CardStatus** — ACTIVE, EXPIRED, BLOCKED
- **AppUserDetails** — security principal

---

## 🧾 REST API Endpoints

### Users (Admin only)
| Method | URL | Description | DTO |
|--------|-----|-------------|-----|
| GET | /api/admin/users | Get all users | UserResponse[] |
| GET | /api/admin/users/{id} | Get user by ID | UserResponse |
| GET | /api/admin/users/by-name/{username} | Get user by username | UserResponse |
| DELETE | /api/admin/users/{id} | Delete user | — |

---

### Auth
| Method | URL | Description | Roles | DTO |
|--------|-----|-------------|-------|-----|
| POST | /api/auth/login | Login | any | LoginRequest |
| POST | /api/auth/register | Register new user | any | RegisterRequest |
| POST | /api/auth/refresh-token | Refresh JWT token | any | RefreshTokenRequest |

**Example request DTOs:**

**RegisterRequest:**
```json
{
  "username": "new_user",
  "email": "new@example.com",
  "password": "pass123",
  "roles": ["USER"]
}
```
**LoginRequest:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```
**RefreshTokenRequest:**
```json
{
  "refreshToken": "<REFRESH_TOKEN>"
}
```

---

### Cards

| Method | URL            | Description         | Roles | DTO                |
|--------|----------------|-------------------|-------|------------------|
| POST   | /api/cards     | Create card        | ADMIN | CardRequest       |
| PUT    | /api/cards/{id}| Update card status | ADMIN | CardStatusRequest |

**Example request DTOs:**

**CardRequest:**
```json
{
  "ownerId": 1
}
```
**CardStatusRequest:**
```json
{
  "ownerId": 1
}
```

---

### Transfers API

| Method | URL           | Description                  | Roles       | DTO             |
|--------|---------------|------------------------------|------------|----------------|
| POST   | /api/transfer | Transfer money between cards | ADMIN, USER| TransferRequest |

**Example request DTOs:**
```json
{
  "fromCardId": 100,
  "toCardId": 101,
  "amount": 50.0
}
```

# 📦 Project Structure

- **Controllers** — REST controllers for API endpoints
- **DTO** — Request classes (Card, Transfer, User, Auth)
- **Services** — Business logic
- **Mappers** — MapStruct mappers for DTO conversion
- **Repositories** — Spring Data JPA repositories
- **Security** — JWT and role-based access
- **Config** — Application configuration

## ⚙️ Configuration

### Docker Compose
```yaml
version: '3'
services:
  postgres:
    image: postgres:12.3
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_DB=bank_db
  redis:
    image: redis:7.0.12
    ports:
      - "6379:6379"
```
### application.yaml

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    open-in-view: false
  datasource:
    url: jdbc:postgresql://localhost:5432/bank_db
    username: postgres
    password: postgres
  liquibase:
    change-log: classpath:db/migration/db.changelog-master.yml
    enabled: true
    drop-first: true
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    url: /docs/openapi.yaml
app:
  jwt:
    secret: "someSecretKey"
    tokenExpiration: 50m
    refreshTokenExpiration: 100m
server:
  port: 8080
```
# 🚀 Local Setup

## Requirements
- Java 17+
- Maven
- Docker & Docker Compose

## Steps
1. Clone the repository:
```bash
git clone <REPO_URL>
cd <PROJECT_FOLDER>
```

### Start PostgreSQL and Redis using Docker Compose
```bash
docker-compose up -d
```
## Edit Configuration

Edit `application.yaml` if needed for your local environment.

## Build and Run the Project
```bash
mvn clean install
mvn spring-boot:run
```

## Access the API
```text
http://localhost:8080
```
## Swagger UI
```text
http://localhost:8080/swagger-ui.html
```
## ⚡ Notes
- All requests use DTOs
- Role-based access ensures Admin/User separation
- JWT authentication
- Redis caching for faster queries
