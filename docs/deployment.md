# Deployment

## Overview

The application supports two deployment modes:

| Mode | Description |
|---|---|
| **Docker Compose** | Local development / quick start with MySQL + API containers |
| **Kubernetes** | Production deployment on Magalu Cloud (2 replicas, LoadBalancer) |

The full CI/CD pipeline is automated via **GitHub Actions** (`.github/workflows/ci.yml`).

---

## Docker Compose

Starts two containers: `mysql-container` and `helpdesk-rest-api`. MySQL health is checked before the API container starts.

```bash
docker compose up -d
```

```mermaid
graph LR
    subgraph Host["Host Machine"]
        CLIENT["HTTP Client\n:8080"]
    end

    subgraph Compose["Docker Compose Network"]
        API["helpdesk-rest-api\nimage: togtec/helpdesk-rest-api:latest\nport: 8080\nprofile: dev\nddl-auto: create"]
        MYSQL["mysql-container\nimage: mysql:8.0\nport: 3306\nDB: helpdeskDB"]
    end

    VOLUME[("💾 mysql-data\n(named volume)")]

    CLIENT -->|"HTTP :8080"| API
    API -->|"JDBC :3306\n(depends_on: healthy)"| MYSQL
    MYSQL --> VOLUME
```

**Environment variables set by Compose:**

| Variable | Value |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://mysql:3306/helpdeskDB` |
| `SPRING_DATASOURCE_USERNAME` | `helpdeskUser` |
| `SPRING_DATASOURCE_PASSWORD` | `admin` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `create` — recreates schema and seeds data on every start |

---

## Kubernetes (Magalu Cloud)

Production deployment in the `helpdesk` namespace. The API is exposed externally via a `LoadBalancer` service on port `8081` (forwarded to container port `8080`).

```mermaid
graph LR
    subgraph Internet
        USER["🌐 External Traffic\n:8081"]
    end

    subgraph K8s["Kubernetes Cluster · namespace: helpdesk"]
        SVC["Service\ntype: LoadBalancer\nport: 8081 → 8080"]

        subgraph Deploy["Deployment · 2 replicas"]
            POD1["Pod 1\nhelpdesk container\n:8080"]
            POD2["Pod 2\nhelpdesk container\n:8080"]
        end

        REG["🗄️ Magalu Cloud\nContainer Registry\ncontainer-registry.br-se1.magalu.cloud"]
    end

    subgraph DB["External Database"]
        MYSQL[("MySQL 8\nhelpdeskDB")]
    end

    USER -->|"HTTP"| SVC
    SVC --> POD1
    SVC --> POD2
    REG -->|"imagePull\n(magalu-registry-secret)"| POD1
    REG -->|"imagePull\n(magalu-registry-secret)"| POD2
    POD1 -->|"JDBC"| MYSQL
    POD2 -->|"JDBC"| MYSQL
```

**Resource limits per pod:**

| | CPU | Memory |
|---|---|---|
| Request | `100m` | `256Mi` |
| Limit | `500m` | `512Mi` |

**Health probes:**

| Probe | Path | Behaviour |
|---|---|---|
| `startupProbe` | `/actuator/health` | 12 attempts × 5s — allows slow JVM startup |
| `livenessProbe` | `/actuator/health/liveness` | Every 10s after 30s delay — restarts unhealthy pods |
| `readinessProbe` | `/actuator/health/readiness` | Every 5s after 10s delay — removes pod from LB if not ready |

---

## CI/CD Pipeline (GitHub Actions)

The workflow in [`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs on every `push` or `pull_request` targeting `main`.

```mermaid
graph TD
    PUSH["git push / Pull Request\n→ main"]

    subgraph Always["Always (PR + push)"]
        A1["☕ Setup Java 17\n(Temurin + Maven cache)"]
        A2["🧪 ./mvnw clean verify\n(tests + build)"]
        A3["🐳 Setup Docker Buildx"]
        A4["🏷️ Generate image metadata\n(tag: sha-short)"]
    end

    subgraph PROnly["Pull Request only"]
        B1["🔨 Build Docker image\n(push: false)"]
    end

    subgraph PushMain["Push to main only"]
        C1["🔐 Login to Magalu Cloud\nContainer Registry"]
        C2["⚙️ Configure kubectl\n(K8S_KUBECONFIG secret)"]
        C3["✅ Test K8s access\nkubectl get pods / auth can-i"]
        C4["🚀 Build & push image\nto Magalu Registry"]
        C5["🔄 kubectl set image\ndeployment/helpdesk"]
        C6["⏳ kubectl rollout status\n--timeout=120s"]
        C7["📋 Publish summary\nimage + digest"]
    end

    PUSH --> A1 --> A2 --> A3 --> A4
    A4 -->|"event: pull_request"| B1
    A4 -->|"event: push + ref: main"| C1
    C1 --> C2 --> C3 --> C4 --> C5 --> C6 --> C7
```

**Required GitHub Secrets:**

| Secret | Description |
|---|---|
| `MGC_REGISTRY_USER` | Magalu Cloud Container Registry username |
| `MGC_REGISTRY_PASSWORD` | Magalu Cloud Container Registry password |
| `K8S_KUBECONFIG` | Full kubeconfig file content for cluster access |
