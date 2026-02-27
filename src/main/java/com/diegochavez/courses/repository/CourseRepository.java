package com.diegochavez.courses.repository;

import com.diegochavez.courses.model.Course;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository {

    private static final String FIND_ALL_SQL = """
            SELECT id, code, title, description, level, duration_h, active, created_at
            FROM courses
            ORDER BY id
            LIMIT ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = (rs, rowNum) -> new Course(
            rs.getLong("id"),
            rs.getString("code"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("level"),
            rs.getObject("duration_h", Integer.class),
            rs.getObject("active", Boolean.class),
            rs.getObject("created_at", OffsetDateTime.class)
    );

    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("null")
    public List<Course> findAll(int limit) {
        return jdbcTemplate.query(FIND_ALL_SQL, courseRowMapper, limit);
    }
}
