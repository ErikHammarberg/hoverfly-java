package io.specto.hoverfly.ruletest;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static io.specto.hoverfly.junit.core.SimulationSource.classpath;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class SpyModeTest {

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSpyMode(classpath("test-service.json"));

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void shouldBeAbleToSimulate() throws Exception {
        // From File
        final ResponseEntity<String> getBookingResponse = restTemplate.getForEntity("https://www.my-test.com/api/bookings/1", String.class);

        assertThat(getBookingResponse.getStatusCode()).isEqualTo(OK);

        // From DSL
        hoverflyRule.simulate(dsl(service("www.other-anotherservice.com")
                .put("/api/bookings/1").body("{\"flightId\": \"1\", \"class\": \"PREMIUM\"}")
                .willReturn(success())));

        final RequestEntity<String> bookFlightRequest = RequestEntity.put(new URI("http://www.other-anotherservice.com/api/bookings/1"))
                .contentType(APPLICATION_JSON)
                .body("{\"flightId\": \"1\", \"class\": \"PREMIUM\"}");

        // When
        final ResponseEntity<String> bookFlightResponse = restTemplate.exchange(bookFlightRequest, String.class);

        // Then
        assertThat(bookFlightResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void shouldBeAbleToSpyRealService() {
        // When
        final ResponseEntity<Void> responseEntity = restTemplate.getForEntity("https://hoverfly.io", Void.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
    }
}
