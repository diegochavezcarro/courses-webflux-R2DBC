# Tasks: WebFlux/R2DBC behavior-preserving refactor for `/courses`

**Input**: Design documents from `/specs/001-webflux-r2dbc-refactor/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are required for this feature because the scope is a behavior-preserving refactor.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Prepare project dependencies and base configuration for reactive stack.

- [ ] T001 Replace MVC/JDBC dependencies with WebFlux/R2DBC dependencies in pom.xml
- [ ] T002 Configure reactive database connection settings in src/main/resources/application.yml
- [ ] T003 Create test profile for deterministic local runs in src/test/resources/application-test.yml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared test and data infrastructure required before implementing user stories.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T004 Create deterministic test data helper for `courses` rows in src/test/java/com/diegochavez/courses/CourseTestDataSupport.java
- [ ] T005 Create shared WebFlux integration test base configuration in src/test/java/com/diegochavez/courses/AbstractCoursesIntegrationTest.java
- [ ] T006 [P] Create response contract assertion utility for `Course` payloads in src/test/java/com/diegochavez/courses/CourseContractAssertions.java

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Consume existing /courses contract unchanged (Priority: P1) 🎯 MVP

**Goal**: Migrate `/courses` implementation to WebFlux/R2DBC while preserving successful response contract.

**Independent Test**: Execute endpoint tests for default and explicit `limit` and verify HTTP 200, response field names, and `id` ascending order.

### Tests for User Story 1

- [ ] T007 [P] [US1] Add endpoint contract tests for default and explicit `limit` in src/test/java/com/diegochavez/courses/CourseEndpointContractTest.java
- [ ] T008 [P] [US1] Add repository integration tests for `ORDER BY id` and `LIMIT` behavior in src/test/java/com/diegochavez/courses/CourseRepositoryR2dbcTest.java

### Implementation for User Story 1

- [ ] T009 [US1] Refactor repository query to reactive R2DBC with `ORDER BY id LIMIT :limit` in src/main/java/com/diegochavez/courses/repository/CourseRepository.java
- [ ] T010 [US1] Refactor service to reactive flow (`Flux<Course>`) in src/main/java/com/diegochavez/courses/service/CourseService.java
- [ ] T011 [US1] Refactor controller to WebFlux response preserving `/courses` and default `limit=100` in src/main/java/com/diegochavez/courses/controller/CourseController.java
- [ ] T012 [US1] Keep `Course` serialization contract unchanged during reactive migration in src/main/java/com/diegochavez/courses/model/Course.java

**Checkpoint**: User Story 1 is functional and independently testable

---

## Phase 4: User Story 2 - Preserve query parameter behavior and error semantics (Priority: P2)

**Goal**: Preserve baseline behavior for invalid and boundary `limit` values.

**Independent Test**: Run parameterized tests for non-numeric, zero, and negative `limit` values and compare status/payload behavior with baseline expectations.

### Tests for User Story 2

- [ ] T013 [P] [US2] Add non-numeric `limit` compatibility tests in src/test/java/com/diegochavez/courses/CourseEndpointLimitErrorCompatibilityTest.java
- [ ] T014 [P] [US2] Add zero/negative `limit` compatibility tests in src/test/java/com/diegochavez/courses/CourseEndpointLimitBoundaryCompatibilityTest.java

### Implementation for User Story 2

- [ ] T015 [US2] Preserve baseline request-parameter parsing behavior for `limit` in src/main/java/com/diegochavez/courses/controller/CourseController.java
- [ ] T016 [US2] Preserve baseline repository behavior for non-positive `limit` inputs in src/main/java/com/diegochavez/courses/repository/CourseRepository.java

**Checkpoint**: User Story 2 is functional and independently testable

---

## Phase 5: User Story 3 - Keep operations and local developer workflow stable (Priority: P3)

**Goal**: Ensure local run/test workflow remains deterministic with manually started DB.

**Independent Test**: From a clean checkout and running DB, execute documented commands and verify app startup and test suite pass.

### Tests for User Story 3

- [ ] T017 [P] [US3] Add reactive application startup smoke test in src/test/java/com/diegochavez/courses/CoursesMvcJdbcApplicationTests.java
- [ ] T018 [P] [US3] Add data isolation test for setup/cleanup determinism in src/test/java/com/diegochavez/courses/CourseIntegrationDataIsolationTest.java

### Implementation for User Story 3

- [ ] T019 [US3] Finalize test profile overrides for manual local DB execution in src/test/resources/application-test.yml
- [ ] T020 [US3] Update test and app run instructions in README.md
- [ ] T021 [US3] Align feature quickstart with final commands and constraints in specs/001-webflux-r2dbc-refactor/quickstart.md

**Checkpoint**: User Story 3 is functional and independently testable

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final verification and cleanup across stories.

- [ ] T022 [P] Add final contract regression assertions for `/courses` payload fields in src/test/java/com/diegochavez/courses/CourseEndpointContractTest.java
- [ ] T023 Verify no `k6/` files changed and keep constraint note in specs/001-webflux-r2dbc-refactor/plan.md
- [ ] T024 Run and document full local verification steps in specs/001-webflux-r2dbc-refactor/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: Depend on Foundational completion
- **Polish (Phase 6)**: Depends on all user stories being complete

### User Story Dependencies

- **US1 (P1)**: Starts after Phase 2; no dependency on other user stories
- **US2 (P2)**: Starts after US1 reactive flow is in place (T009-T011)
- **US3 (P3)**: Starts after US1 and can run in parallel with late US2 validation

### Within Each User Story

- Tests before implementation
- Repository/service/controller changes in that order when applicable
- Story checkpoint before moving forward

### Parallel Opportunities

- T006 can run in parallel with T004-T005
- US1: T007 and T008 run in parallel
- US2: T013 and T014 run in parallel
- US3: T017 and T018 run in parallel
- Polish: T022 can run in parallel with T023

---

## Parallel Example: User Story 1

```bash
Task: "Add endpoint contract tests for default and explicit `limit` in src/test/java/com/diegochavez/courses/CourseEndpointContractTest.java"
Task: "Add repository integration tests for `ORDER BY id` and `LIMIT` behavior in src/test/java/com/diegochavez/courses/CourseRepositoryR2dbcTest.java"
```

## Parallel Example: User Story 2

```bash
Task: "Add non-numeric `limit` compatibility tests in src/test/java/com/diegochavez/courses/CourseEndpointLimitErrorCompatibilityTest.java"
Task: "Add zero/negative `limit` compatibility tests in src/test/java/com/diegochavez/courses/CourseEndpointLimitBoundaryCompatibilityTest.java"
```

## Parallel Example: User Story 3

```bash
Task: "Add reactive application startup smoke test in src/test/java/com/diegochavez/courses/CoursesMvcJdbcApplicationTests.java"
Task: "Add data isolation test for setup/cleanup determinism in src/test/java/com/diegochavez/courses/CourseIntegrationDataIsolationTest.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 and Phase 2
2. Complete Phase 3 (US1)
3. Validate US1 endpoint contract behavior
4. Demo/review before continuing

### Incremental Delivery

1. Deliver US1 (reactive parity)
2. Deliver US2 (error/boundary parity)
3. Deliver US3 (operational stability + docs)
4. Run final polish verification

### Parallel Team Strategy

1. Engineer A: repository/service/controller migration (US1)
2. Engineer B: endpoint and compatibility tests (US1/US2)
3. Engineer C: deterministic test infrastructure + docs (Phase 2/US3)

---

## Notes

- Tasks follow strict checklist format with IDs, optional `[P]`, and `[USx]` labels in story phases
- Do not modify files under `k6/`
- Do not run k6 or stress/performance tests for this feature
- Keep API contract and behavior identical to baseline unless explicitly requested
