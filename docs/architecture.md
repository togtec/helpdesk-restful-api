# Architecture

## Overview

**Helpdesk REST API** is a Spring Boot 3 RESTful application that manages support tickets (_chamados_) between clients (_clientes_) and technicians (_técnicos_). Authentication is handled via self-issued JWT (HMAC-HS512), and authorization is role-based using Spring Security method-level annotations.

---

## C1 — System Context

Shows the system as a black box and who interacts with it.

```mermaid
graph TD
    Admin["👤 Admin\n(manages technicians)"]
    Tecnico["👤 Técnico\n(opens & updates tickets)"]
    Cliente["👤 Cliente\n(views own tickets)"]

    API["🖥️ Helpdesk REST API\n(Spring Boot 3 · Java 17)"]

    Admin -->|"REST / JWT"| API
    Tecnico -->|"REST / JWT"| API
    Cliente -->|"REST / JWT"| API

    API -->|"reads/writes"| DB[("💾 Database\nMySQL 8 · H2 in-memory")]
```

> **External actors**
> | Actor | Description |
> |---|---|
> | Admin | Creates and manages technicians |
> | Técnico | Opens, lists and updates support tickets; reads client data |
> | Cliente | Self-registers (no auth required); views and manages own data and tickets |

---

## C2 — Container Diagram

Opens the system box and shows the internal containers (processes / runtimes).

```mermaid
graph TD
    subgraph Client["Client Layer"]
        HTTP["HTTP Client\n(Postman / Frontend / curl)"]
    end

    subgraph API["Spring Boot Application · :8080"]
        direction TB
        SEC["SecurityFilterChain\n(JWT OAuth2 Resource Server\nstateless · CORS · CSRF disabled)"]
        CTR["Controllers\n(REST endpoints)\n/login · /clientes · /tecnicos · /chamados"]
        SVC["Services\n(Business Logic)\nClienteService · TecnicoService\nChamadoService · TokenService"]
        REPO["Repositories\n(Spring Data JPA)\nPessoaRepository · ChamadoRepository\nClienteRepository · TecnicoRepository"]
        ERR["Exception Handler\n(@ControllerAdvice)\n404 · 409 · 422"]
    end

    subgraph Infra["Infrastructure"]
        MYSQL[("MySQL 8\nhelpdeskDB · :3306\n[profile: dev]")]
        H2[("H2 In-Memory\n[profile: test]")]
    end

    HTTP -->|"HTTP Request"| SEC
    SEC -->|"authenticated request"| CTR
    CTR -->|"delegates"| SVC
    CTR -->|"error"| ERR
    SVC -->|"queries"| REPO
    REPO -->|"JDBC"| MYSQL
    REPO -->|"JDBC"| H2
```

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.1.0 |
| Security | Spring Security OAuth2 Resource Server · JWT (HMAC-HS512) |
| Persistence | Spring Data JPA · Hibernate |
| Database (dev) | MySQL 8 |
| Database (test) | H2 in-memory |
| API Docs | SpringDoc OpenAPI 2.6.0 (`/swagger-ui.html`) |
| Build | Maven Wrapper (`./mvnw`) |
| Containerization | Docker · Docker Compose |
| Orchestration | Kubernetes (Magalu Cloud) |
