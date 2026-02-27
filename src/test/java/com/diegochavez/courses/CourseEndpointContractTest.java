package com.diegochavez.courses;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

class CourseEndpointContractTest extends AbstractCoursesIntegrationTest {

    @Test
    void shouldReturnCoursesWithDefaultLimitAndContractShape() {
        courseTestDataSupport.seedDefaultCourses();

        List<Map<String, Object>> body = webTestClient.get()
                .uri("/courses")
                .exchange()
                .expectStatus().isOk()
            .expectBodyList(new ParameterizedTypeReference<Map<String, Object>>() {
            })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isEmpty());
        body.forEach(CourseContractAssertions::assertCourseShape);

        long previousId = Long.MIN_VALUE;
        for (Map<String, Object> item : body) {
            Number id = (Number) item.get("id");
            Assertions.assertNotNull(id);
            Assertions.assertTrue(id.longValue() >= previousId);
            previousId = id.longValue();
        }
    }

    @Test
    void shouldRespectExplicitLimit() {
        courseTestDataSupport.seedDefaultCourses();

        List<Map<String, Object>> body = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/courses").queryParam("limit", "2").build())
                .exchange()
                .expectStatus().isOk()
            .expectBodyList(new ParameterizedTypeReference<Map<String, Object>>() {
            })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(2, body.size());
        body.forEach(CourseContractAssertions::assertCourseShape);
    }
}
