# AGENTS.md — Ask Mode

This file provides guidance to agents answering questions about this repository.

## Non-obvious context

- **Single-table inheritance**: `Tecnico` and `Cliente` share the `TB_PESSOA` table — there is no separate table per subtype. The discriminator column is `tipo`.
- **JWT is self-issued**: There is no external authorization server. `TokenService` issues JWTs signed with the symmetric secret in `application.properties`. The app is simultaneously the auth server and resource server.
- **`/login` is a custom endpoint** (`AuthController`), not Spring Security's default form login. It returns a JWT on successful authentication.
- **H2 console** is available at `/h2-console` only when profile is `test`. It is explicitly disabled for `dev` (no `permitAll` rule added).
- **OpenAPI docs** at `/swagger-ui.html` include a pre-configured Bearer auth scheme — JWT can be pasted directly in the Swagger UI to test protected endpoints.
- **Actuator** only exposes `health` and `info`; liveness/readiness probes are enabled for Kubernetes (`/actuator/health/liveness`, `/actuator/health/readiness`).
- **`git-commit-id-maven-plugin`** populates `/actuator/info` with git metadata at build time.
