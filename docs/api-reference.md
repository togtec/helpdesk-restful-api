# API Reference

> 💡 A interactive documentation is also available at `/swagger-ui.html` when the application is running.

---

## Base URL

```
http://localhost:8080
```

---

## Authentication

All endpoints except the ones marked as **Public** require a `Bearer` JWT token in the `Authorization` header:

```
Authorization: Bearer <token>
```

Obtain a token via `POST /login`. See [authentication.md](authentication.md) for the full flow.

---

## Error Responses

| HTTP Status | Code | When |
|---|---|---|
| `404 Not Found` | `NOT_FOUND` | Entity not found by ID |
| `409 Conflict` | `CONFLICT` | CPF or e-mail already registered |
| `422 Unprocessable Entity` | `UNPROCESSABLE_ENTITY` | Bean Validation failure (missing/invalid fields) |

**Error body (404 / 409):**
```json
{
  "timestamp": 1700000000000,
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Objeto não encontrado! ID:99",
  "path": "/clientes/99"
}
```

**Validation error body (422):**
```json
{
  "timestamp": 1700000000000,
  "status": 422,
  "error": "UNPROCESSABLE_ENTITY",
  "message": "Erro na validação dos campos",
  "path": "/clientes",
  "errors": [
    { "field": "cpf", "message": "invalid Brazilian individual taxpayer registry number (CPF)" }
  ]
}
```

---

## Endpoints

### Auth

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/login` | Public | Authenticate and receive a JWT token |

**Request body:**
```json
{ "username": "user@email.com", "password": "plain-password" }
```

**Response:** `200 OK` — plain JWT string.

---

### Clientes

| Method | Path | Required Role | Description |
|---|---|---|---|
| `POST` | `/clientes` | **Public** | Self-register a new client |
| `GET` | `/clientes/{id}` | `ROLE_CLIENTE` | Get client by ID |
| `GET` | `/clientes` | `ROLE_TECNICO` | List all clients |
| `PUT` | `/clientes/{id}` | `ROLE_CLIENTE` | Update client data |
| `DELETE` | `/clientes/{id}` | `ROLE_CLIENTE` | Delete client |

**Request body (`POST` / `PUT`):**
```json
{
  "nome": "Maria Silva",
  "cpf": "000.000.000-00",
  "email": "maria@email.com",
  "senha": "123456"
}
```

**Response body:**
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "cpf": "000.000.000-00",
  "email": "maria@email.com",
  "perfis": [1],
  "dataCriacao": "01/01/2024"
}
```

---

### Técnicos

| Method | Path | Required Role | Description |
|---|---|---|---|
| `POST` | `/tecnicos` | `ROLE_ADMIN` | Create a new technician |
| `GET` | `/tecnicos/{id}` | `ROLE_TECNICO` | Get technician by ID |
| `GET` | `/tecnicos` | `ROLE_TECNICO` | List all technicians |
| `PUT` | `/tecnicos/{id}` | `ROLE_ADMIN` | Update technician data |
| `DELETE` | `/tecnicos/{id}` | `ROLE_ADMIN` | Delete technician |

**Request body (`POST` / `PUT`):**
```json
{
  "nome": "João Técnico",
  "cpf": "111.111.111-11",
  "email": "joao@email.com",
  "senha": "123456"
}
```

> Roles `ROLE_CLIENTE` (1) and `ROLE_TECNICO` (2) are always assigned automatically — no need to send them in the request.

**Response body:**
```json
{
  "id": 2,
  "nome": "João Técnico",
  "cpf": "111.111.111-11",
  "email": "joao@email.com",
  "perfis": [1, 2],
  "dataCriacao": "01/01/2024"
}
```

---

### Chamados

| Method | Path | Required Role | Description |
|---|---|---|---|
| `POST` | `/chamados` | `ROLE_TECNICO` | Open a new ticket |
| `GET` | `/chamados/{id}` | `ROLE_CLIENTE` | Get ticket by ID |
| `GET` | `/chamados` | `ROLE_TECNICO` | List all tickets |
| `PUT` | `/chamados/{id}` | `ROLE_TECNICO` | Update ticket |

> Only a `Técnico` can open or update tickets. The client is referenced in the body (`cliente` field) but does not create tickets directly.

**Request body (`POST` / `PUT`):**
```json
{
  "prioridade": 1,
  "status": 0,
  "titulo": "Problema com impressora",
  "observacoes": "A impressora não liga desde ontem.",
  "cliente": 1,
  "tecnico": 2
}
```

| Field | Type | Values |
|---|---|---|
| `prioridade` | `Integer` | `0` = BAIXA · `1` = MEDIA · `2` = ALTA |
| `status` | `Integer` | `0` = ABERTO · `1` = ANDAMENTO · `2` = ENCERRADO |
| `cliente` | `Long` | ID of an existing client |
| `tecnico` | `Long` | ID of an existing technician |

**Response body:**
```json
{
  "id": 1,
  "dataAbertura": "15/01/2024",
  "dataFechamento": null,
  "prioridade": 1,
  "status": 0,
  "titulo": "Problema com impressora",
  "observacoes": "A impressora não liga desde ontem.",
  "cliente": 1,
  "tecnico": 2,
  "nomeCliente": "Maria Silva",
  "nomeTecnico": "João Técnico"
}
```

> When `status` is set to `2` (ENCERRADO), `dataFechamento` is automatically set to today's date by the service.
