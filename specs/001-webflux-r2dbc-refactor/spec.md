# Feature Specification: Refactor /courses to WebFlux + R2DBC

**Feature Branch**: `001-webflux-r2dbc-refactor`  
**Created**: 2026-02-27  
**Status**: Draft  
**Input**: User description: "Refactor /courses API from Spring MVC + JDBC to Spring WebFlux + R2DBC preserving exact behavior and API contract"

## User Scenarios & Testing *(mandatory)*

<!--
  IMPORTANT: User stories should be PRIORITIZED as user journeys ordered by importance.
  Each user story/journey must be INDEPENDENTLY TESTABLE - meaning if you implement just ONE of them,
  you should still have a viable MVP (Minimum Viable Product) that delivers value.
  
  Assign priorities (P1, P2, P3, etc.) to each story, where P1 is the most critical.
  Think of each story as a standalone slice of functionality that can be:
  - Developed independently
  - Tested independently
  - Deployed independently
  - Demonstrated to users independently
-->

### User Story 1 - Consume existing /courses contract unchanged (Priority: P1)

As an API consumer, I can continue calling `GET /courses` with the `limit` query parameter and receive the same response shape and status behavior as before the refactor.

**Why this priority**: Preserving current client integrations is the primary business objective of this refactor.

**Independent Test**: Can be fully tested by issuing requests to `GET /courses` with and without `limit` and asserting status codes, JSON field names, ordering, and default behavior.

**Acceptance Scenarios**:

1. **Given** the API is running with existing course data, **When** a client requests `GET /courses` without query params, **Then** the API returns HTTP 200 and a JSON array of courses using the current field names.
2. **Given** the API is running with existing course data, **When** a client requests `GET /courses?limit=25`, **Then** the API returns HTTP 200 with at most 25 courses ordered exactly as in current behavior.

---

### User Story 2 - Preserve query parameter behavior and error semantics (Priority: P2)

As an API consumer, I receive the same outcomes for valid and invalid `limit` inputs so client-side error handling and expectations remain unchanged.

**Why this priority**: Input handling differences are a common regression source during framework changes and can break consumers even if endpoint paths are unchanged.

**Independent Test**: Can be fully tested with parameterized requests for omitted, valid numeric, and invalid non-numeric `limit` values and comparing status behavior with baseline.

**Acceptance Scenarios**:

1. **Given** the API is running, **When** a client sends a non-numeric `limit` value, **Then** the API returns the same error status family and contract behavior as the pre-refactor implementation.

---

### User Story 3 - Keep operations and local developer workflow stable (Priority: P3)

As a developer/operator, I can run the service and automated tests locally against a manually started database without new infrastructure steps.

**Why this priority**: Migration success requires low operational friction and repeatable local verification.

**Independent Test**: Can be tested by running the documented test command and app start command on a local machine with a manually started database.

**Acceptance Scenarios**:

1. **Given** a manually started database with existing schema, **When** the developer runs the test suite, **Then** tests execute deterministically and validate `/courses` compatibility.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- `limit` is omitted (default behavior must match current implementation).
- `limit` is non-numeric (error behavior and status outcome must match current implementation).
- `limit` is zero or negative (observed behavior must remain unchanged).
- Database returns no rows (must return the same empty-result response contract as current API).
- Nullable DB fields (`duration_h`, `active`, `created_at`) must preserve current serialization behavior.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: System MUST expose exactly one public endpoint for this feature scope: `GET /courses`.
- **FR-002**: System MUST support the `limit` query parameter with default value `100` when omitted.
- **FR-003**: System MUST return course records with the same response field set currently exposed (`id`, `code`, `title`, `description`, `level`, `durationH`, `active`, `createdAt`).
- **FR-004**: System MUST preserve current ordering semantics for `/courses` results.
- **FR-005**: System MUST preserve current status code behavior and observable error behavior for valid and invalid `limit` requests.
- **FR-006**: System MUST use the existing database schema without requiring schema changes or data migrations.
- **FR-007**: System MUST allow local execution with a manually started database and no required container or infrastructure provisioning.

### Compatibility & Non-Regression Requirements *(mandatory for existing systems)*

- **CNR-001**: Existing public API endpoint path `/courses` MUST remain unchanged.
- **CNR-002**: Request parameter name (`limit`), response payload shape, and status behavior MUST remain backward compatible.
- **CNR-003**: Scope MUST NOT add authentication, authorization, new endpoints, or additional product features.
- **CNR-004**: Java language level, toolchain, and build configuration MUST target Java 21.
- **CNR-005**: Work scope MUST NOT modify files under `k6/` and MUST NOT include running k6 or any stress/performance tests.
- **CNR-006**: Database schema and data model contracts MUST remain as-is unless a strictly required refactor blocker is identified.

### Key Entities *(include if feature involves data)*

- **Course**: Represents a single catalog course returned by `/courses`, with identifier, metadata, optional duration/active flags, and creation timestamp.
- **CourseQuery**: Represents request intent for listing courses, currently defined by optional `limit` and defaulting behavior.

## Assumptions

- Current `/courses` behavior observed from the existing MVC/JDBC implementation is the source of truth for compatibility.
- The database is started manually by the user before running the app/tests.
- Deterministic tests may use controlled test data setup against a local database without introducing new infrastructure tooling.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: 100% of contract tests for `GET /courses` pass for baseline scenarios (default limit, explicit limit, invalid limit).
- **SC-002**: 100% of regression tests confirm identical JSON field names and response shape compared to the current implementation.
- **SC-003**: 100% of automated tests for this feature pass on a local machine with a manually started database.
- **SC-004**: Developer can run tests and start the app using documented commands in under 10 minutes from a clean checkout with DB already running.
