# Data Model

## Overview

The persistence layer uses **JPA with a SINGLE_TABLE inheritance strategy**. `Pessoa` is an abstract entity mapped to the table `TB_PESSOA`; both `Tecnico` and `Cliente` are concrete subclasses stored in the same table, differentiated by a discriminator column `tipo`.

Roles (perfis) are stored as a collection of integers in a separate join table `TB_PERFIL`, decoupled from the enum names to allow stable persistence across refactors.

---

## Entity-Relationship Diagram

```mermaid
erDiagram
    TB_PESSOA {
        BIGINT      pessoa_id       PK
        VARCHAR(80) nome            "NOT NULL"
        VARCHAR(14) cpf             "NOT NULL, UNIQUE"
        VARCHAR(80) email           "NOT NULL, UNIQUE"
        VARCHAR(60) senha           "NOT NULL (BCrypt)"
        DATE        data_criacao    "NOT NULL"
        VARCHAR(31) tipo            "discriminator: Tecnico | Cliente"
    }

    TB_PERFIL {
        BIGINT  pessoa_id   FK
        INT     perfil      "0=ADMIN · 1=CLIENTE · 2=TECNICO"
    }

    TB_CHAMADO {
        BIGINT       chamado_id      PK
        DATE         data_abertura   "NOT NULL"
        DATE         data_fechamento "nullable"
        INT          prioridade      "0=BAIXA · 1=MEDIA · 2=ALTA"
        INT          status          "0=ABERTO · 1=ANDAMENTO · 2=ENCERRADO"
        VARCHAR(255) titulo          "NOT NULL"
        TEXT         observacoes     "nullable"
        BIGINT       cliente_id      FK "client the ticket refers to"
        BIGINT       tecnico_id      FK "technician who opened the ticket"
    }

    TB_PESSOA ||--o{ TB_PERFIL   : "has roles"
    TB_PESSOA ||--o{ TB_CHAMADO  : "Tecnico opens (tecnico_id)"
    TB_PESSOA }o--o{ TB_CHAMADO  : "Cliente referenced (cliente_id)"
```

> ⚠️ **Important:** only a `Técnico` can open a ticket (`POST /chamados` requires `ROLE_TECNICO`).
> The `cliente_id` field is a reference to the client the ticket is about — the client never creates tickets directly.
> Clients contact technicians via external channels (phone, e-mail, etc.), and the technician opens the ticket on their behalf.

---

## Inheritance Strategy

```mermaid
graph TD
    Pessoa["Pessoa (abstract)\n@Entity · TB_PESSOA\n@Inheritance SINGLE_TABLE\ndiscriminator: tipo"]

    Tecnico["Tecnico\n@DiscriminatorValue('Tecnico')\nperfis: CLIENTE + TECNICO\n(always both)"]

    Cliente["Cliente\n@DiscriminatorValue('Cliente')\nperfis: CLIENTE"]

    Pessoa --> Tecnico
    Pessoa --> Cliente
```

> **Key rule:** when a `Tecnico` is created via `TecnicoService`, both `Perfil.CLIENTE` (1) and `Perfil.TECNICO` (2) are added automatically — a technician is always also a client.

---

## Enums (stored as Integer)

All enums are persisted by their integer `codigo`, never by name. Mapping is always done via `XxxEnum.toEnum(Integer)`.

### Perfil

| Código | Constante | Descrição |
|---|---|---|
| 0 | `ADMIN` | `ROLE_ADMIN` |
| 1 | `CLIENTE` | `ROLE_CLIENTE` |
| 2 | `TECNICO` | `ROLE_TECNICO` |

### Prioridade

| Código | Constante |
|---|---|
| 0 | `BAIXA` |
| 1 | `MEDIA` |
| 2 | `ALTA` |

### Status

| Código | Constante | Behaviour |
|---|---|---|
| 0 | `ABERTO` | `dataFechamento` is `null` |
| 1 | `ANDAMENTO` | `dataFechamento` is `null` |
| 2 | `ENCERRADO` | `dataFechamento` = `LocalDate.now()` |

---

## Date Format

All `LocalDate` fields are serialized/deserialized as `"dd/MM/yyyy"` via `@JsonFormat(pattern = "dd/MM/yyyy")`.
