# Implementation Plan: WebFlux/R2DBC behavior-preserving refactor for `/courses`

**Branch**: `001-webflux-r2dbc-refactor` | **Date**: 2026-02-27 | **Spec**: `/specs/001-webflux-r2dbc-refactor/spec.md`
**Input**: Feature specification from `/specs/001-webflux-r2dbc-refactor/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Refactor the existing `/courses` read endpoint from Spring MVC + JDBC to Spring WebFlux + R2DBC while preserving exact API behavior: same path, query parameter semantics (`limit`), response fields, ordering (`id` ascending), and status/error behavior for invalid inputs. The implementation remains minimal, keeps existing schema unchanged, and adds deterministic automated tests using controlled per-test data setup/cleanup.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 21  
**Primary Dependencies**: Spring Boot 3.5.x, `spring-boot-starter-webflux`, `spring-boot-starter-data-r2dbc`, PostgreSQL R2DBC driver (`org.postgresql:r2dbc-postgresql`), Reactor  
**Storage**: PostgreSQL (existing schema, no migrations in scope)  
**Testing**: JUnit 5, Spring Boot Test, WebTestClient, Reactor Test  
**Target Platform**: Spring Boot service on local/server JVM (Java 21)
**Project Type**: Web service (single backend application)  
**Performance Goals**: Preserve functional behavior; no performance target change in this refactor  
**Constraints**: No API contract changes; no auth additions; no schema changes; no `k6/` edits; no k6 execution  
**Scale/Scope**: Single endpoint refactor (`GET /courses`) plus focused regression tests and run documentation

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Check

- **API Contract Stability**: PASS — plan keeps `/courses` path, `limit` semantics, status behavior, and response shape unchanged.
- **Java 21 Baseline**: PASS — current `pom.xml` already targets Java 21; refactor keeps Java 21.
- **Spring Boot Standards**: PASS — constructor injection and layered flow maintained in reactive form.
- **Automated Testing**: PASS — add/update endpoint and integration tests with deterministic data setup/cleanup.
- **Behavior Preservation**: PASS — invariants defined for default `limit`, invalid/non-positive `limit`, and ordering by `id` ascending.
- **No-k6 Rule**: PASS — `k6/` excluded from scope, no performance test execution in plan.
- **Minimal Diff Scope**: PASS — limited to controller/service/repository/config/dependencies/tests/docs for this endpoint.

### Post-Phase 1 Design Re-check

- **API Contract Stability**: PASS — contract artifact defines unchanged request/response behavior and ordering invariant.
- **Java 21 Baseline**: PASS — technical context and quickstart lock Java 21 tooling.
- **Spring Boot Standards**: PASS — design uses WebFlux + R2DBC idioms without introducing unrelated architectural changes.
- **Automated Testing**: PASS — design includes WebTestClient endpoint tests and R2DBC-backed integration coverage.
- **Behavior Preservation**: PASS — research and data model include explicit baseline-preservation decisions.
- **No-k6 Rule**: PASS — explicitly captured in research, constraints, quickstart.
- **Minimal Diff Scope**: PASS — design avoids new endpoints, auth, infra, and schema migration work.

## Project Structure

### Documentation (this feature)

```text
specs/001-webflux-r2dbc-refactor/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
src/
├── main/
│   ├── java/com/diegochavez/courses/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
└── test/java/com/diegochavez/courses/

specs/001-webflux-r2dbc-refactor/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── contracts/

k6/                  # Explicitly out of scope for this feature
```

**Structure Decision**: Use the existing single Spring Boot service structure and apply a minimal in-place refactor from MVC/JDBC classes to WebFlux/R2DBC equivalents. Keep package names stable and add focused tests under `src/test/java`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

No constitution violations identified.
