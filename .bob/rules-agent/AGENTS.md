# AGENTS.md — Agent/Code Mode

This file provides guidance to agents when writing or modifying code in this repository.

## Non-obvious coding rules

- **Enum integer codes**: `Perfil` (ADMIN=0, CLIENTE=1, TECNICO=2), `Prioridade`, `Status` — all stored as `Integer` in DB/DTOs. Always use `Xxx.toEnum(Integer)` for mapping. Never persist by name.
- **Tecnico always gets CLIENTE + TECNICO perfis**: `TecnicoService.create()` always adds both `Perfil.CLIENTE` and `Perfil.TECNICO` automatically. Tests or seeds that create a Tecnico without calling the service must add both manually.
- **Passwords always BCrypt-encoded in service layer** — never store raw passwords. `TecnicoService` and `ClienteService` call `encoder.encode()` before saving. Test constants use plain strings because tests mock the service, not the repository.
- **`Pessoa.perfis` field is `Set<Integer>`**, not `Set<Perfil>`. Use `addPerfil(Perfil p)` which stores `p.getCodigo()`. Calling `resetPerfis()` clears the set entirely (needed before update to avoid stale roles).
- **`ChamadoService.newChamado()`**: sets `dataFechamento = LocalDate.now()` only when `status == 2` (ENCERRADO). This is an integer comparison, not enum.
- **`/clientes` POST is public** (no auth required) — the only mutating public endpoint. All others require JWT.
- **JwtDecoder uses "RSA" as algorithm label** for the `SecretKeySpec` constructor but the actual MAC is HS512. This is a known quirk in the config — do not change it.
- **No Lombok** — all models and DTOs use manual getters/setters. Add them explicitly when creating new fields.
- **No `@Valid` in services** — validation annotations on DTOs are only enforced by Spring MVC in controllers. Service-layer tests that call services directly bypass Bean Validation.
