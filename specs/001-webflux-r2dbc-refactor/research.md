# Research: WebFlux + R2DBC refactor for `/courses`

## Decision 1: Keep annotated controller style while returning reactive types

- **Decision**: Use `@RestController` + `@GetMapping` and return `Flux<Course>` from `/courses`.
- **Rationale**: Preserves existing endpoint mapping structure and minimizes diff while adopting WebFlux runtime behavior.
- **Alternatives considered**:
  - Functional routing (`RouterFunction`/`HandlerFunction`) — rejected to avoid unnecessary architectural churn.
  - Keep MVC with async wrappers — rejected because migration goal is explicit WebFlux refactor.

## Decision 2: Use Spring Data R2DBC repository with explicit SQL query

- **Decision**: Replace `JdbcTemplate` repository with reactive data access using `DatabaseClient` or `R2dbcEntityTemplate` and explicit SQL preserving `ORDER BY id LIMIT :limit` behavior.
- **Rationale**: Maintains exact query semantics (ordering and limit) and maps cleanly to existing table/schema without migrations.
- **Alternatives considered**:
  - Derived query methods on `ReactiveCrudRepository` only — rejected because exact SQL control is required for strict baseline parity.
  - ORM-style schema/model redesign — rejected due to no-schema-change constraint.

## Decision 3: Preserve baseline input/error behavior for `limit`

- **Decision**: Treat omitted `limit`, non-numeric `limit`, and `limit<=0` exactly as current baseline behavior (status and payload shape).
- **Rationale**: Contract stability is the highest priority and was explicitly clarified in spec sessions.
- **Alternatives considered**:
  - Normalize invalid values to default limit — rejected due to behavior drift.
  - Introduce new standardized error contract — rejected because API shape must not change.

## Decision 4: Deterministic integration tests with controlled test data

- **Decision**: Seed and clean test-owned rows per integration test case against manually started PostgreSQL, and validate endpoint behavior via `WebTestClient`.
- **Rationale**: Deterministic, locally runnable tests without introducing Docker/Testcontainers (out of scope).
- **Alternatives considered**:
  - Depend on pre-existing manual DB data — rejected due to flakiness and non-repeatability.
  - Full mocking of data layer in endpoint tests only — rejected because reactive DB integration behavior must be verified.

## Decision 5: Dependency and configuration migration scope

- **Decision**: Update dependencies from MVC/JDBC to WebFlux/R2DBC, keep Java 21, and adjust `application.yml` to R2DBC connection settings while keeping DB host/schema assumptions unchanged.
- **Rationale**: Meets migration goal with minimal scope and no infra/schema changes.
- **Alternatives considered**:
  - Dual-stack (keep both MVC/JDBC and WebFlux/R2DBC) — rejected to avoid ambiguity and unnecessary complexity in a small-scope refactor.
  - Add Flyway/Liquibase migrations — rejected because schema changes are explicitly out of scope.

## Decision 6: k6 isolation

- **Decision**: Exclude `k6/` from all modifications and from validation commands.
- **Rationale**: Explicit hard constraint in user request and constitution.
- **Alternatives considered**:
  - Run comparative load tests after refactor — rejected for this phase; may be done later only if explicitly requested.