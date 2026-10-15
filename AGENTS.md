# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## Stack
- Java 17 + Spring Boot 3.1.0, Maven wrapper (`./mvnw`)
- Auth: Spring Security OAuth2 Resource Server with self-issued JWT (HMAC-HS512, symmetric secret key)
- DB: H2 in-memory (profile `test`) / MySQL 8 (profile `dev`)
- Docs: SpringDoc OpenAPI 2.6.0 (`/swagger-ui.html`)

## Commands
```bash
./mvnw spring-boot:run                          # run (uses profile=test by default)
./mvnw test                                     # all tests
./mvnw test -Dtest=ChamadoServiceTest           # single test class
./mvnw test -Dtest=ChamadoServiceTest#createChamado_WithValidData_ReturnsChamado  # single method
./mvnw package -DskipTests                      # build JAR
```
Default active profile in `application.properties` is `test` (H2). Switch to `dev` via env var `SPRING_PROFILES_ACTIVE=dev` or editing the property.

## Architecture
- `Pessoa` (abstract, SINGLE_TABLE inheritance) → `Tecnico` / `Cliente` — single table `TB_PESSOA` with discriminator column `tipo`
- Perfis stored as `Set<Integer>` in `TB_PERFIL`; always retrieved via `Perfil.toEnum(Integer)` — never stored as enum directly
- DTOs split by direction: `*RequestDTO` (write), `*ResponseDTO` (read), `ChamadoDTO` (both)
- `DBService.instanciaDB()` seeds test/dev data; called by `TestConfig` (@Profile test) and `DevConfig` (@Profile dev, only when `ddl-auto=create`)

## Critical Patterns

### Enum encoding
All enums (`Perfil`, `Prioridade`, `Status`) use integer codes. API receives/sends `Integer`, service maps via `XxxEnum.toEnum(Integer)`. **Never pass the enum name as a string.**

### Security — method-level authorization
Endpoints are guarded with `@PreAuthorize("hasAuthority('SCOPE_ROLE_XXX')")`. The `SCOPE_` prefix is required because Spring Security OAuth2 JWT automatically adds it. JWT roles come from the `scope` claim.

### Service pattern — ID override
Services **always force `id = null` on create** and **force `id = pathVariable` on update** to prevent body-injection attacks. Do not remove these lines.

### Validation error HTTP codes
- `ObjectNotFoundException` → 404
- `MethodArgumentNotValidException` (Bean Validation) → 422
- `DataIntegrityViolationException` (custom) → 409

### Date format
All `LocalDate` fields serialized as `"dd/MM/yyyy"` via `@JsonFormat`. Use this pattern in all new date fields.

## Testing
- Unit tests use JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`) — no Spring context loaded
- Test constants live in `src/test/java/.../common/` (`ChamadoConstants`, `ClienteConstants`, `TecnicoContants` — note the typo in `TecnicoContants`)
- Test naming convention: `methodName_StateUnderTest_ExpectedBehavior`

## Dev Environment
- Docker Compose (`docker-compose.yml`) starts MySQL + API. Sets `SPRING_JPA_HIBERNATE_DDL_AUTO=create` which triggers seed data
- Actuator exposes only `health` and `info` — do not add other endpoints without updating `SecurityConfig`
