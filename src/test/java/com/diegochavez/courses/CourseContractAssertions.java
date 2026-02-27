package com.diegochavez.courses;

import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class CourseContractAssertions {

    private CourseContractAssertions() {
    }

    public static void assertCourseShape(Map<String, Object> payload) {
        Assertions.assertTrue(payload.containsKey("id"));
        Assertions.assertTrue(payload.containsKey("code"));
        Assertions.assertTrue(payload.containsKey("title"));
        Assertions.assertTrue(payload.containsKey("description"));
        Assertions.assertTrue(payload.containsKey("level"));
        Assertions.assertTrue(payload.containsKey("durationH"));
        Assertions.assertTrue(payload.containsKey("active"));
        Assertions.assertTrue(payload.containsKey("createdAt"));
    }
}
