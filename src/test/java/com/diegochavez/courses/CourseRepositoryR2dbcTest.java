package com.diegochavez.courses;

import com.diegochavez.courses.model.Course;
import com.diegochavez.courses.repository.CourseRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CourseRepositoryR2dbcTest extends AbstractCoursesIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldReturnCoursesOrderedByIdWithLimit() {
        courseTestDataSupport.seedDefaultCourses();

        Mono<List<Course>> result = courseRepository.findAll(2).collectList();

        StepVerifier.create(result)
                .assertNext(courses -> {
                    Assertions.assertEquals(2, courses.size());
                    Assertions.assertTrue(courses.get(0).id() <= courses.get(1).id());
                })
                .verifyComplete();
    }
}
