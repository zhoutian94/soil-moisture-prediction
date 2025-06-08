package com.example.loran_service.service;

import com.example.loran_service.TimeOfArrival;
import com.example.loran_service.service.impl.LoranServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoranServiceImplTest {

    @Autowired
    private LoranServiceImpl loranService;

    @Test
    public void shouldReturnTimeOfArrivalForValidCoordinates() {
//        double lat = 36.12;
//        double lon = 71.12;
        TimeOfArrival toa = loranService.getTOA(36.5773,109.4487);
//        TimeOfArrival toa = loranService.getTOA(lat, lon);
        assertThat(toa).isNotNull();
        assertThat(toa.getDelay()).isPositive();
    }
}
