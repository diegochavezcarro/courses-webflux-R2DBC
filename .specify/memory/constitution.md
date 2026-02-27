<!--
Sync Impact Report
- Version change: N/A (template) → 1.0.0
- Modified principles:
	- Template Principle 1 → I. API Contract Stability
	- Template Principle 2 → II. Java 21 and Spring Boot Standards
	- Template Principle 3 → III. Automated Testing Is Mandatory
	- Template Principle 4 → IV. Behavior-Preserving Refactors
	- Template Principle 5 → V. Minimal Scope and Controlled Diffs
- Added sections:
	- Engineering Constraints
	- Delivery Workflow
- Removed sections:
	- None
- Templates requiring updates:
	- ✅ updated: .specify/templates/plan-template.md
	- ✅ updated: .specify/templates/spec-template.md
	- ✅ updated: .specify/templates/tasks-template.md
	- ⚠ pending: .specify/templates/commands/*.md (directory not present in this repository)
- Follow-up TODOs:
	- None
-->

# courses-webflux-R2DBC Constitution

## Core Principles

### I. API Contract Stability
Existing HTTP contracts MUST remain backward compatible unless a change is explicitly
requested. Endpoints, request and response shapes, and status codes MUST NOT change during
refactors or framework migrations. Rationale: this project is used for comparative runtime and
load testing, so contract drift invalidates baseline comparability.

### II. Java 21 and Spring Boot Standards
All code, build configuration, and toolchain settings MUST target Java 21 unless explicitly
overridden by user request. Code changes MUST follow Spring Boot 3.x conventions: constructor
injection, layered architecture (controller/service/repository), boundary validation, and
configuration via `application.yml` or type-safe properties. Field injection and deprecated
Spring Boot 2.x patterns MUST NOT be introduced. Rationale: keeps runtime behavior predictable
and aligned with local engineering guides under `.agents/skills/spring-boot-engineer/`.

### III. Automated Testing Is Mandatory
Every behavior change or refactor MUST include automated tests that prove unchanged or expected
behavior, and all relevant tests MUST pass before merge. At minimum, maintain existing test
coverage and add focused tests when modifying controller, service, or repository behavior.
Rationale: protects regression-sensitive API and performance scenarios.

### IV. Behavior-Preserving Refactors
Refactors (including MVC ↔ WebFlux or persistence-layer changes) MUST preserve observable
behavior unless the user explicitly requests functional change. No new product features,
authentication flows, endpoints, or infrastructure changes may be introduced implicitly.
Rationale: ensures migrations and cleanups are safe, reviewable, and measurable.

### V. Minimal Scope and Controlled Diffs
Implementations MUST be the smallest viable change set that solves the stated request.
Unrelated cleanup, style-only churn, or opportunistic architectural expansion MUST be avoided.
Rationale: minimal diffs reduce review risk and keep intent traceable.

## Engineering Constraints

- Runtime baseline MUST remain Maven + Spring Boot with the existing project structure.
- Java language level, compiler target, and toolchain MUST be Java 21 unless explicitly waived.
- Public API paths and response semantics MUST be documented and validated through tests.
- New dependencies SHOULD be avoided unless required to satisfy an explicit requirement, and any
	added dependency MUST be justified in the implementation plan.
- Files under `k6/` MUST NOT be modified as part of regular feature/refactor work.
- k6 stress/performance tests MUST NOT be executed unless explicitly requested by the user.

## Delivery Workflow

1. Define scope from the user request and map impacted layers (controller/service/repository).
2. Identify contract and behavior invariants before coding.
3. Confirm Java 21 baseline for language level and build/toolchain configuration.
4. Confirm no planned edits under `k6/` and no k6 execution in the validation plan.
5. Implement minimal code changes aligned with Spring Boot engineering guides.
6. Add or update automated tests for touched behavior.
7. Run targeted tests first, then broader regression checks when risk warrants.
8. Update README or related docs when commands, behavior, or constraints change.

## Governance

This constitution supersedes conflicting local conventions for engineering decisions in this
repository.

Amendment process:
- Changes MUST be proposed in a PR with a clear rationale and a Sync Impact Report.
- At least one reviewer MUST confirm template and workflow alignment.
- Ratification occurs on merge of the amendment PR.

Versioning policy:
- MAJOR: incompatible governance changes or principle removals/redefinitions.
- MINOR: new principle/section or materially expanded mandatory guidance.
- PATCH: wording clarifications, typo fixes, or non-semantic refinements.

Compliance review expectations:
- Every plan MUST pass Constitution Check gates before implementation.
- Every spec MUST declare contract impact and test obligations.
- Every task list MUST include testing work for behavior changes and refactors.
- Any Java version change away from 21 MUST include explicit user-approved justification.
- Plans and tasks MUST explicitly respect the no-k6-modification and no-k6-execution rule.

**Version**: 1.0.0 | **Ratified**: 2026-02-27 | **Last Amended**: 2026-02-27
