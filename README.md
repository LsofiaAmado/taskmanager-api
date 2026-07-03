# 📋 Task Manager API

A RESTful API developed with Spring Boot for managing personal tasks securely using JWT authentication.

The application allows users to register, log in, create tasks, update them, delete them, search, filter, paginate, and manage their own information securely.

---

## 🚀 Technologies

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- Docker
- Docker Compose
- Maven
- Lombok
- Swagger / OpenAPI
- JUnit 5
- Mockito

---

## ✨ Features

- User registration
- User authentication (JWT)
- CRUD operations for tasks
- Search tasks
- Filter by status
- Filter by priority
- Pagination
- Sorting
- Input validation
- Exception handling
- Unit Tests
- Integration Tests
- Docker support
- Swagger documentation

---

## 🏗️ Project Structure

src
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service

---

## ⚙️ Running the project locally

Clone the repository

```bash
git clone <repository-url>
```

Move into the project

```bash
cd taskmanagerapi
```

Compile

```bash
mvn clean package
```

Run

```bash
mvn spring-boot:run
```

---

## 🐳 Running with Docker

Build and start

```bash
docker compose up --build
```

Stop containers

```bash
docker compose down
```

---

## 📖 API Documentation

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 🗄️ Database

PostgreSQL

Hibernate

Spring Data JPA

---

## 🔐 Authentication

JWT (JSON Web Token)

Protected endpoints require:

```
Authorization: Bearer <token>
```

---

## 👩‍💻 Author

Developed by **Sofy Ama**

Backend Developer | Java | Spring Boot