[English version](README.md)

# 🏦 Система Банковских Карт

Система Банковских Карт — это backend-решение для управления картами, переводами и пользователями с REST API. Система поддерживает ролевой доступ (ADMIN/USER), операции CRUD с картами, переводы и кэширование через Redis.

---

## 🔍 Основные функции

- Управление картами и пользователями
- Ролевая аутентификация (Admin/User)
- REST API для функционала CMS панели
- Переводы между картами
- Кэширование через Redis для быстрого доступа
- DTO для объектов запросов

---

## ⚙️ Технологический стек

- Backend: Java 21+, Spring Boot, Spring Data JPA, MapStruct, Validation
- База данных: PostgreSQL, Liquibase
- Кэширование: Redis
- Безопасность: Spring Security, JWT
- DevOps: Docker, Docker Compose
- Утилиты: Lombok, Maven

---

## 🧩 Сущности

- **User** — управление пользователями и аутентификация
- **Card** — карта с балансом, статусом и владельцем
- **TransferRequest** — DTO для перевода между картами
- **CardStatus** — ACTIVE, EXPIRED, BLOCKED
- **AppUserDetails** — security principal

---

## 🧾 REST API Endpoints

### Пользователи (только для Admin)
| Method | URL | Описание | DTO |
|--------|-----|----------|-----|
| GET | /api/admin/users | Получить всех пользователей | UserResponse[] |
| GET | /api/admin/users/{id} | Получить пользователя по ID | UserResponse |
| GET | /api/admin/users/by-name/{username} | Получить пользователя по username | UserResponse |
| DELETE | /api/admin/users/{id} | Удалить пользователя | — |

---

### Аутентификация
| Method | URL | Описание | Роли | DTO |
|--------|-----|----------|------|-----|
| POST | /api/auth/login | Вход | любой | LoginRequest |
| POST | /api/auth/register | Регистрация нового пользователя | любой | RegisterRequest |
| POST | /api/auth/refresh-token | Обновление JWT токена | любой | RefreshTokenRequest |

**Примеры DTO запросов:**

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

# Карты

| Method | URL             | Описание               | Роли  | DTO                |
|--------|----------------|----------------------|-------|------------------|
| POST   | /api/cards     | Создать карту         | ADMIN | CardRequest       |
| PUT    | /api/cards/{id}| Изменить статус карты | ADMIN | CardStatusRequest |

**Примеры DTO запросов:**

**CardRequest:**
```json
{
  "ownerId": 1
}
```
**CardStatusRequest:**

```json
{
  "status": "BLOCKED"
}
```

---

### Переводы

| Method | URL           | Описание               | Роли       | DTO             |
|--------|---------------|----------------------|------------|----------------|
| POST   | /api/transfer | Перевод между картами | ADMIN, USER| TransferRequest |

**Пример DTO запроса:**
```json
{
  "fromCardId": 100,
  "toCardId": 101,
  "amount": 50.0
}
```

# 📦 Структура проекта

- **Controllers** — REST контроллеры для API  
- **DTO** — классы запросов (Card, Transfer, User, Auth)  
- **Services** — бизнес-логика  
- **Mappers** — MapStruct мапперы для конвертации DTO  
- **Repositories** — Spring Data JPA репозитории  
- **Security** — JWT и ролевой доступ  
- **Config** — конфигурация приложения  

## ⚙️ Конфигурация

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
      ddl-auto: validate 
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
## 🧪 Testing

The project features a comprehensive testing suite (30+ tests) to ensure the stability of critical banking operations.

* **Unit Testing:** Isolated business logic validation for services (`CardService`, `TransferService`, `AdminUserService`) using **Mockito**.
* **Integration Testing:** API layer validation via **MockMvc** with simulated security contexts.
* **Key Approaches:**
    * **Base Abstract Classes:** Leveraging `AbstractControllerTest` to maintain clean and dry test code.
    * **Resource-Based Assertions:** Using external JSON files to strictly validate API response contracts.
    * **Advanced Assertions:** Utilizing **JsonUnit** for precise JSON structure comparison.

# 🚀 Локальный запуск

## Требования
- Java 17+
- Maven
- Docker & Docker Compose

## Шаги

1. Клонирование репозитория
```bash
git clone <REPO_URL>
cd <PROJECT_FOLDER>
```
### Запуск PostgreSQL и Redis через Docker Compose
```bash
docker-compose up -d
```
## Редактирование конфигурации

Отредактируйте `application.yaml`, если нужно для вашего локального окружения.

## Сборка и запуск проекта
```bash
mvn clean install
mvn spring-boot:run
```

## Доступ к API
```text
http://localhost:8080
```
## Swagger UI
```text
http://localhost:8080/swagger-ui.html
```
## ⚡ Примечания
- Контроль структуры БД — миграции через Liquibase (отказ от hibernate ddl-auto) для безопасного обновления схемы без потери данных.
- Интерактивная документация — полный контракт API в Swagger/OpenAPI с возможностью тестирования JWT-авторизации прямо из браузера.
- Безопасность и роли — четкое разделение доступа (Admin/User) на уровне Spring Security + использование JWT (Access & Refresh tokens).
- Производительность — оптимизация доступа к данным через кэширование в Redis.
- Чистая архитектура — строгий обмен данными через DTO и валидация входящих запросов на уровне контроллеров.
