package com.diegochavez.courses;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

class CourseEndpointLimitBoundaryCompatibilityTest extends AbstractCoursesIntegrationTest {

    @Test
    void shouldReturnEmptyForZeroLimit() {
        courseTestDataSupport.seedDefaultCourses();

        List<Map<String, Object>> body = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/courses").queryParam("limit", "0").build())
                .exchange()
                .expectStatus().isOk()
            .expectBodyList(new ParameterizedTypeReference<Map<String, Object>>() {
            })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.isEmpty());
    }

    @Test
    void shouldPreserveServerErrorForNegativeLimit() {
        courseTestDataSupport.seedDefaultCourses();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/courses").queryParam("limit", "-1").build())
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
