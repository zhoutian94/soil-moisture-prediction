package com.example.loran_service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoranServiceTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldReturnATimeOfArrival() {
        TOARequest request = new TOARequest(36.5773,109.4487);
        ResponseEntity<TimeOfArrival> response = restTemplate.postForEntity("/api/toa", request, TimeOfArrival.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDelay()).isPositive();
    }
}
