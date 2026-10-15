# AGENTS.md — Plan Mode

This file provides guidance to agents planning changes to this repository.

## Non-obvious architectural constraints

- **SINGLE_TABLE inheritance is load-bearing**: `Tecnico` and `Cliente` share `TB_PESSOA`. Any new person subtype must extend `Pessoa` and add a discriminator value. Splitting into separate tables requires a migration.
- **`PessoaRepository` is the cross-cutting uniqueness guard**: CPF and email uniqueness across both `Tecnico` and `Cliente` is enforced by querying `PessoaRepository` (not the subtype repositories). New person types must reuse this same pattern.
- **Profile-driven seed data**: Both `DevConfig` and `TestConfig` call `DBService.instanciaDB()`. Adding new seed entities requires changing only `DBService` — not the config classes.
- **No delete on `Chamado`**: `ChamadoService` has no `delete` method by design. Chamados are immutable records after creation; only status/fields can be updated.
- **`dev` profile requires MySQL via Docker Compose** (`docker-compose.yml`). The `ddl-auto=create` in Docker Compose re-seeds the DB on every container restart.
- **CORS is wide open** (`applyPermitDefaultValues()` + all methods) — intended for dev/demo. Restrict before production.
- **Security uses method-level `@PreAuthorize`**, not URL-pattern roles. Adding new endpoints requires explicitly annotating with `@PreAuthorize("hasAuthority('SCOPE_ROLE_XXX')")` or they default to requiring any valid JWT (`anyRequest().authenticated()`).
