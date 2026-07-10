# 🚀 Task Manager API

REST API desarrollada con **Spring Boot** para la gestión de tareas con autenticación JWT, control de acceso mediante Spring Security, persistencia en PostgreSQL y despliegue en la nube con Render.

Este proyecto fue desarrollado como parte de mi portafolio Backend Java para demostrar conocimientos en el desarrollo de APIs seguras, pruebas automatizadas, Docker y buenas prácticas de desarrollo.

---

## ✨ Características

- 🔐 Registro e inicio de sesión con JWT.
- 👤 Autenticación y autorización con Spring Security.
- ✅ CRUD completo de tareas.
- 📄 Paginación y ordenamiento.
- 🔍 Búsqueda de tareas por palabra clave.
- 🎯 Filtros por estado y prioridad.
- 📝 Validación de datos.
- ⚠️  Manejo centralizado de excepciones.
- 📚 Documentación automática con Swagger/OpenAPI.
- 🐳 Contenedorización con Docker y Docker Compose.
- ☁️ Despliegue en Render.
- 🧪 Pruebas unitarias e integración.
- 📈 Cobertura de pruebas con JaCoCo.
- 🔄 Integración continua con GitHub Actions.
- 📋 Logging de operaciones importantes.

---

# 🛠 Tecnologías

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- Docker Compose
- Swagger / OpenAPI
- JUnit 5
- Mockito
- JaCoCo
- GitHub Actions
- Render

---

# 📂 Estructura del proyecto

```
src
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── security
├── service
└── util
```

---

# ▶️ Ejecución local

### Clonar el repositorio

```bash
git clone https://github.com/LsofiaAmado/taskmanager-api.git
```

### Entrar al proyecto

```bash
cd taskmanager-api
```

### Ejecutar con Maven

```bash
mvn spring-boot:run
```

---

# 🐳 Ejecutar con Docker

Construir la imagen:

```bash
mvn clean package
```

```bash
docker compose up --build
```

---

# 📖 Documentación de la API

Swagger UI:

**https://taskmanager-api-z4mf.onrender.com/swagger-ui/index.html**

API Docs:

**https://taskmanager-api-z4mf.onrender.com/v3/api-docs**

---

# ☁️ Aplicación desplegada

Render:

**https://taskmanager-api-z4mf.onrender.com**

---

# 🧪 Pruebas

El proyecto incluye:

- Pruebas unitarias con JUnit 5 y Mockito.
- Pruebas de integración con MockMvc.
- Cobertura de código con JaCoCo.

**Cobertura aproximada: 97%** ✅

---

# 🔄 Integración Continua

El proyecto utiliza **GitHub Actions** para:

- Compilar el proyecto.
- Ejecutar las pruebas automáticamente.
- Validar cada cambio enviado al repositorio principal.

---

# 📋 Funcionalidades principales

## Autenticación

- POST /auth/register
- POST /auth/login

## Gestión de tareas

- POST /tasks
- GET /tasks
- GET /tasks/{id}
- PUT /tasks/{id}
- DELETE /tasks/{id}

## Funcionalidades adicionales

- GET /tasks/page
- GET /tasks/search
- GET /tasks/filter
- GET /tasks/status/{status}
- GET /tasks/priority/{priority}
- PATCH /tasks/{id}/status

---

# 👩‍💻 Autor

**Laura Sofia Amado **

GitHub:
https://github.com/LsofiaAmado

LinkedIn:
https://www.linkedin.com/in/laura-sofia-amado-gonzalez-bb5b87222/
