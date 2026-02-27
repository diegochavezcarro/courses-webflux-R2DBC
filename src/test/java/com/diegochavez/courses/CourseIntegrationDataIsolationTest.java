package com.diegochavez.courses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CourseIntegrationDataIsolationTest extends AbstractCoursesIntegrationTest {

    @Test
    void shouldCleanupOnlyOwnedRowsBetweenTests() {
        courseTestDataSupport.seedDefaultCourses();

        long countAfterSeed = courseTestDataSupport.countOwnedCourses();
        Assertions.assertTrue(countAfterSeed > 0);

        courseTestDataSupport.cleanupOwnedCourses();

        long countAfterCleanup = courseTestDataSupport.countOwnedCourses();
        Assertions.assertEquals(0L, countAfterCleanup);
    }
}
