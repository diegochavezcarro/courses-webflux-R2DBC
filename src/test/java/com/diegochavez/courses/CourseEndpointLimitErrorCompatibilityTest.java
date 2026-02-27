package com.diegochavez.courses;

import org.junit.jupiter.api.Test;

class CourseEndpointLimitErrorCompatibilityTest extends AbstractCoursesIntegrationTest {

    @Test
    void shouldReturnBadRequestForNonNumericLimit() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/courses").queryParam("limit", "abc").build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
