# Quickstart: WebFlux + R2DBC `/courses` refactor

## Prerequisites

- Java 21 available (`java --version`)
- PostgreSQL running manually with existing `coursesdb` schema and `courses` table
- Credentials aligned with `application.yml`

## 1) Run tests

```bash
./mvnw test
```

Run a specific test class:

```bash
./mvnw -Dtest=CoursesMvcJdbcApplicationTests test
```

## 2) Run the app

```bash
./mvnw spring-boot:run
```

## 3) Verify endpoint behavior

```bash
curl "http://localhost:8080/courses?limit=100"
```

## Constraints for this feature

- Do not modify files under `k6/`
- Do not run k6 or stress/performance tests as part of this refactor validation
- Preserve exact `/courses` API behavior (contract, status, response shape)