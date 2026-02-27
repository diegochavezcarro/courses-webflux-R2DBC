package com.diegochavez.courses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "RUN_DB_TESTS", matches = "true")
abstract class AbstractCoursesIntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected CourseTestDataSupport courseTestDataSupport;

    @BeforeEach
    void cleanOwnedRows() {
        courseTestDataSupport.cleanupOwnedCourses();
    }
}
