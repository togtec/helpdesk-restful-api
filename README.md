# Helpdesk REST API

A production-ready **Spring Boot 3** REST API for support ticket management, featuring stateless JWT authentication, role-based authorization, full CI/CD automation, and Kubernetes deployment on Magalu Cloud.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Data Model](#data-model)
- [Authentication & Authorization](#authentication--authorization)
- [API Reference](#api-reference)
- [Running Locally](#running-locally)
- [Running with Docker](#running-with-docker)
- [Testing with Postman](#testing-with-postman)
- [CI/CD Pipeline](#cicd-pipeline)
- [Documentation](#documentation)

---

## Overview

The **Helpdesk REST API** is the backend for a support ticket management system. It handles three types of users — **Clients**, **Technicians**, and **Administrators** — each with clearly scoped permissions. The API enforces **authentication on every endpoint** (except client self-registration) using self-issued JWTs signed with **HMAC-HS512**.

Key highlights:

- **Stateless security** — no sessions, no cookies; every request carries a `Bearer` token
- **Role-based access control** — method-level `@PreAuthorize` on every protected endpoint
- **Two startup profiles** — `test` (H2 in-memory) and `dev` (MySQL 8)
- **Comprehensive unit tests** — JUnit 5 + Mockito, zero Spring context overhead
- **Swagger UI** available at `/swagger-ui.html` when the app is running
- **Containerized** — Docker Compose for local dev, Kubernetes for production
- **Fully automated CI/CD** — GitHub Actions builds, tests, pushes image, and rolls out to Kubernetes on every push to `main`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.1.0 |
| Security | Spring Security · OAuth2 Resource Server · JWT (HMAC-HS512) |
| Persistence | Spring Data JPA · Hibernate |
| Database (dev) | MySQL 8 |
| Database (test) | H2 in-memory |
| API Docs | SpringDoc OpenAPI 2.6.0 (`/swagger-ui.html`) |
| Testing | JUnit 5 · Mockito |
| Build | Maven Wrapper (`./mvnw`) |
| Containerization | Docker · Docker Compose |
| Orchestration | Kubernetes (Magalu Cloud) |
| CI/CD | GitHub Actions |

---

## Architecture

The application follows a classic layered architecture: **Controller → Service → Repository**. Security is enforced at the filter chain level before requests reach any controller.

```
┌──────────────────────────────────────────────────────┐
│                Spring Boot Application :8080         │
│                                                      │
│  SecurityFilterChain (JWT OAuth2 Resource Server)    │
│          ↓ authenticated request                     │
│  Controllers  →  Services  →  Repositories           │
│                      ↓                               │
│              @ControllerAdvice (error handling)      │
└──────────────────────────────────────────────────────┘
         ↓ JDBC
┌─────────────────────────┐
│  MySQL 8 (profile: dev) │
│  H2 in-memory (test)    │
└─────────────────────────┘
```

> For full C1/C2 diagrams see [`docs/architecture.md`](docs/architecture.md).

---

## Data Model

`Pessoa` is an abstract JPA entity using **SINGLE_TABLE inheritance** — both `Tecnico` and `Cliente` share the `TB_PESSOA` table, differentiated by a discriminator column. Roles are stored as integers in `TB_PERFIL`, decoupled from enum names.

```
TB_PESSOA          TB_PERFIL         TB_CHAMADO
──────────         ──────────        ──────────────────
pessoa_id (PK)     pessoa_id (FK)    chamado_id (PK)
nome               perfil            titulo
cpf (UNIQUE)         0 = ADMIN       prioridade  (0/1/2)
email (UNIQUE)       1 = CLIENTE     status      (0/1/2)
senha (BCrypt)       2 = TECNICO     data_abertura
data_criacao                         data_fechamento
tipo (discriminator)                 cliente_id (FK)
                                     tecnico_id (FK)
```

> **Rule:** a `Tecnico` always holds **both** `ROLE_CLIENTE` and `ROLE_TECNICO`. Technicians can do everything clients can, plus more.

> For the full ER diagram and enum tables see [`docs/data-model.md`](docs/data-model.md).

---

## Authentication & Authorization

### Login Flow

```
POST /login  { "username": "email", "password": "plain" }
      ↓
AuthenticationManager → BCrypt verify → TokenService
      ↓
200 OK — plain JWT string
```

The JWT payload carries a `scope` claim with the user's roles (e.g. `ROLE_TECNICO ROLE_CLIENTE`). Spring Security automatically prefixes each scope with `SCOPE_`, which is why controllers use `@PreAuthorize("hasAuthority('SCOPE_ROLE_TECNICO')")`.

### Authorization Matrix

| Role | Endpoints |
|---|---|
| `ROLE_ADMIN` | `POST /tecnicos` · `PUT /tecnicos/{id}` · `DELETE /tecnicos/{id}` |
| `ROLE_TECNICO` | `GET /tecnicos` · `GET /tecnicos/{id}` · `GET /clientes` · `POST /chamados` · `GET /chamados` · `PUT /chamados/{id}` |
| `ROLE_CLIENTE` | `GET /clientes/{id}` · `PUT /clientes/{id}` · `DELETE /clientes/{id}` · `GET /chamados/{id}` |
| *(public)* | `POST /login` · `POST /clientes` · `GET /swagger-ui.html` · `GET /actuator/health` |

> For sequence diagrams and JWT structure details see [`docs/authentication.md`](docs/authentication.md).

---

## API Reference

### Tickets (`/chamados`)

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/chamados` | `ROLE_TECNICO` | Open a new ticket |
| `GET` | `/chamados` | `ROLE_TECNICO` | List all tickets |
| `GET` | `/chamados/{id}` | `ROLE_CLIENTE` | Get ticket by ID |
| `PUT` | `/chamados/{id}` | `ROLE_TECNICO` | Update ticket |

### Clients (`/clientes`)

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/clientes` | **Public** | Self-register |
| `GET` | `/clientes` | `ROLE_TECNICO` | List all clients |
| `GET` | `/clientes/{id}` | `ROLE_CLIENTE` | Get client by ID |
| `PUT` | `/clientes/{id}` | `ROLE_CLIENTE` | Update client |
| `DELETE` | `/clientes/{id}` | `ROLE_CLIENTE` | Delete client |

### Technicians (`/tecnicos`)

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/tecnicos` | `ROLE_ADMIN` | Create technician |
| `GET` | `/tecnicos` | `ROLE_TECNICO` | List all technicians |
| `GET` | `/tecnicos/{id}` | `ROLE_TECNICO` | Get technician by ID |
| `PUT` | `/tecnicos/{id}` | `ROLE_ADMIN` | Update technician |
| `DELETE` | `/tecnicos/{id}` | `ROLE_ADMIN` | Delete technician |

> For full request/response bodies and error codes see [`docs/api-reference.md`](docs/api-reference.md).

---

## Running Locally

Requires Java 17 and Maven (or use the included Maven Wrapper).

```bash
# Clone the repository
git clone https://github.com/togtec/helpdesk-restful-api.git
cd helpdesk-restful-api

# Run with the H2 in-memory database (no external dependencies)
./mvnw spring-boot:run

# Run all unit tests
./mvnw test

# Build a JAR (skipping tests)
./mvnw package -DskipTests
```

The application starts on `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

> The default active profile is `test` (H2). Switch to MySQL by setting `SPRING_PROFILES_ACTIVE=dev` and providing a running MySQL instance.

---

## Running with Docker

Starts the full stack (API + MySQL 8) with no local Java or MySQL installation required.

```bash
docker compose up -d
```

The `docker-compose.yml` sets `SPRING_JPA_HIBERNATE_DDL_AUTO=create`, which instructs Hibernate to **drop and recreate all tables on every startup**. The application detects this value and automatically runs the seed, loading 5 technicians, 5 clients, and 6 tickets — ready to explore with Postman immediately.

> ⚠️ **This is the expected behavior for a demo or development environment** — the database is always reset to a clean, known state on every restart. Any data inserted manually will be lost when the API container restarts.

### Keeping data between restarts

If you want to preserve data across restarts, use `update` instead of `create` after the initial seed has been loaded:

```bash
SPRING_JPA_HIBERNATE_DDL_AUTO=update docker compose up -d
```

With `update`, Hibernate only applies schema migrations without dropping existing data. The seed is **not** triggered — [`DevConfig`](src/main/java/com/rodrigo/helpdesk/config/DevConfig.java) only runs it when the value is exactly `"create"` — so your data is preserved across restarts.

For the Docker Compose topology and Kubernetes deployment details see [`docs/deployment.md`](docs/deployment.md).

---

## Testing with Postman

A ready-to-use Postman collection is available at [`postman/helpdesk-restapi.postman_collection.json`](postman/helpdesk-restapi.postman_collection.json). Import it into Postman and all requests are pre-configured — no manual setup needed.

### How to import

1. Open Postman → **Import**
2. Select the file `postman/helpdesk-restapi.postman_collection.json`
3. Start the application (locally or via Docker)
4. Use the credentials below to log in and get a JWT token

### Seed test accounts

All accounts are created automatically when the application starts with `ddl-auto=create`. Every account shares the same password: **`1234`**

#### Admin

One admin account exists. An admin can manage technicians (create, update, delete).

| Name | Email | Password |
|---|---|---|
| Rodrigo Tognetta | `tog@gmail.com` | `1234` |

#### Technicians

Technicians can open and update tickets and view client data. Use any of these accounts to test technician endpoints.

| Name | Email | Password |
|---|---|---|
| Priscila Lopez | `prilopez@gmail.com` | `1234` |
| Alexandre Sanches | `alesanches@gmail.com` | `1234` |
| Mara Cruz | `maracruz@gmail.com` | `1234` |
| Alfredo Costa | `alfcosta@gmail.com` | `1234` |

> Note: the admin account (`tog@gmail.com`) also holds `ROLE_TECNICO` and `ROLE_CLIENTE`, so it can be used to test technician and client endpoints as well.

#### Clients

Clients can view and manage their own data and consult their tickets. Use any of these accounts to test client endpoints.

| Name | Email | Password |
|---|---|---|
| Alessandra Lima | `alelima@gmail.com` | `1234` |
| Flavio Sampaio | `flaviosp@bol.com.br` | `1234` |
| Igor Batista | `ibatista@bol.com.br` | `1234` |
| Paulo Cintra | `paulo.cintra@globo.com` | `1234` |
| Sabrina Coral | `sabcoral@gmail.com` | `1234` |

---

## CI/CD Pipeline

Every push or pull request targeting `main` triggers the GitHub Actions workflow:

```
push / PR → main
      │
      ├─ Setup Java 17 (Temurin + Maven cache)
      ├─ ./mvnw clean verify  (tests + build)
      ├─ Setup Docker Buildx
      │
      ├─ [PR only]   Build Docker image (push: false)
      │
      └─ [push to main]
            ├─ Login to Magalu Cloud Container Registry
            ├─ Configure kubectl (K8S_KUBECONFIG secret)
            ├─ Build & push image
            ├─ kubectl set image deployment/helpdesk
            └─ kubectl rollout status --timeout=120s
```

Production runs on **Kubernetes (Magalu Cloud)** with 2 replicas, a `LoadBalancer` service, and startup/liveness/readiness health probes via `/actuator/health`.

> For the full pipeline diagram and Kubernetes manifest details see [`docs/deployment.md`](docs/deployment.md).

---

## Documentation

| File | Contents |
|---|---|
| [`docs/architecture.md`](docs/architecture.md) | C1 (Context) and C2 (Container) diagrams |
| [`docs/data-model.md`](docs/data-model.md) | ER diagram, JPA inheritance strategy, enum tables |
| [`docs/authentication.md`](docs/authentication.md) | JWT flow, sequence diagrams, authorization matrix |
| [`docs/api-reference.md`](docs/api-reference.md) | All endpoints, request/response bodies, error codes |
| [`docs/deployment.md`](docs/deployment.md) | Docker Compose, Kubernetes, and CI/CD pipeline |
