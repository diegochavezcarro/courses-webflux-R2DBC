package com.diegochavez.courses;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
public class CourseTestDataSupport {

    private static final long OWNED_ID_THRESHOLD = -9_000_000L;

    private final DatabaseClient databaseClient;

    public CourseTestDataSupport(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public void cleanupOwnedCourses() {
        databaseClient.sql("DELETE FROM courses WHERE id <= :threshold")
                .bind("threshold", OWNED_ID_THRESHOLD)
                .fetch()
                .rowsUpdated()
                .block();
    }

    public void seedDefaultCourses() {
        List<TestCourseSeed> seeds = List.of(
                new TestCourseSeed(-9_000_003L, "TST-003", "Reactive Three", "D3", "BEGINNER", 3, true),
                new TestCourseSeed(-9_000_002L, "TST-002", "Reactive Two", "D2", "INTERMEDIATE", 2, true),
                new TestCourseSeed(-9_000_001L, "TST-001", "Reactive One", "D1", "ADVANCED", 1, false)
        );
        for (TestCourseSeed seed : seeds) {
            upsert(seed);
        }
    }

    public long countOwnedCourses() {
        Long count = databaseClient.sql("SELECT COUNT(*) AS total FROM courses WHERE id <= :threshold")
                .bind("threshold", OWNED_ID_THRESHOLD)
                .map((row, metadata) -> row.get("total", Long.class))
                .one()
                .block();
        return count == null ? 0L : count;
    }

    private void upsert(TestCourseSeed seed) {
        databaseClient.sql("""
                INSERT INTO courses (id, code, title, description, level, duration_h, active, created_at)
                VALUES (:id, :code, :title, :description, :level, :durationH, :active, :createdAt)
                ON CONFLICT (id)
                DO UPDATE SET
                    code = EXCLUDED.code,
                    title = EXCLUDED.title,
                    description = EXCLUDED.description,
                    level = EXCLUDED.level,
                    duration_h = EXCLUDED.duration_h,
                    active = EXCLUDED.active,
                    created_at = EXCLUDED.created_at
                """)
                .bind("id", Objects.requireNonNull(seed.id()))
                .bind("code", Objects.requireNonNull(seed.code()))
                .bind("title", Objects.requireNonNull(seed.title()))
                .bind("description", Objects.requireNonNull(seed.description()))
                .bind("level", Objects.requireNonNull(seed.level()))
                .bind("durationH", Objects.requireNonNull(seed.durationH()))
                .bind("active", Objects.requireNonNull(seed.active()))
                .bind("createdAt", Objects.requireNonNull(OffsetDateTime.now()))
                .fetch()
                .rowsUpdated()
                .block();
    }

    private record TestCourseSeed(
            Long id,
            String code,
            String title,
            String description,
            String level,
            Integer durationH,
            Boolean active
    ) {
    }
}
