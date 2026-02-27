# Contract: `GET /courses`

## Endpoint

- **Method**: `GET`
- **Path**: `/courses`
- **Auth**: None (unchanged)

## Query Parameters

- `limit` (optional, integer)
  - Default: `100` when omitted
  - Non-numeric: must preserve baseline error behavior (status + payload shape)
  - `0` or negative: must preserve baseline behavior (status + payload shape)

## Success Response

- **Status**: `200 OK`
- **Content-Type**: `application/json`
- **Body**: JSON array of course objects

### Course Object Shape

```json
{
  "id": 1,
  "code": "CS-101",
  "title": "Intro to CS",
  "description": "...",
  "level": "BEGINNER",
  "durationH": 20,
  "active": true,
  "createdAt": "2026-01-01T10:00:00Z"
}
```

## Behavioral Invariants

- Results are ordered by `id` ascending.
- Endpoint path and query parameter name remain unchanged.
- Response field names remain unchanged.
- No additional envelope/wrapper is introduced.

## Error Compatibility

- For invalid `limit` inputs, status and payload shape must match pre-refactor baseline.
- No new custom error format is introduced in this refactor.

## Out-of-Scope Changes

- No new endpoints.
- No authentication/authorization changes.
- No schema or migration changes.