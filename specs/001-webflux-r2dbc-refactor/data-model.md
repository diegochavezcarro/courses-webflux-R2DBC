# Data Model: `/courses` WebFlux + R2DBC refactor

## Entity: Course

- **Purpose**: Represents one row returned from `courses` table and serialized in `/courses` response.
- **Fields**:
  - `id: Long` (required)
  - `code: String` (required)
  - `title: String` (required)
  - `description: String` (nullable)
  - `level: String` (nullable)
  - `durationH: Integer` (nullable; maps from `duration_h`)
  - `active: Boolean` (nullable)
  - `createdAt: OffsetDateTime` (nullable; maps from `created_at`)
- **Relationships**: None in this feature scope.
- **Validation rules**: No new validation introduced; preserve baseline behavior.

## Entity: CourseQuery

- **Purpose**: Represents read query intent for `/courses`.
- **Fields**:
  - `limit: int` (default `100` when omitted)
- **Rules**:
  - Omitted limit => default to `100`.
  - Non-numeric and non-positive limits => preserve baseline status/payload behavior exactly.

## Read Model and Ordering

- Query returns a stream/list of `Course` values ordered by `id` ascending.
- Result size upper-bounded by `limit` according to baseline behavior.

## State Transitions

- No mutable state transitions are in scope (read-only endpoint).