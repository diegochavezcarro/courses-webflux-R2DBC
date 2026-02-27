package com.diegochavez.courses.repository;

import com.diegochavez.courses.model.Course;
import java.time.OffsetDateTime;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class CourseRepository {

    private static final String FIND_ALL_SQL = """
            SELECT id, code, title, description, level, duration_h, active, created_at
            FROM courses
            ORDER BY id
            LIMIT :limit
            """;

    private final DatabaseClient databaseClient;

    public CourseRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Flux<Course> findAll(int limit) {
        return databaseClient.sql(FIND_ALL_SQL)
                .bind("limit", limit)
                .map((row, metadata) -> new Course(
                        row.get("id", Long.class),
                        row.get("code", String.class),
                        row.get("title", String.class),
                        row.get("description", String.class),
                        row.get("level", String.class),
                        row.get("duration_h", Integer.class),
                        row.get("active", Boolean.class),
                        row.get("created_at", OffsetDateTime.class)
                ))
                .all();
    }
}
