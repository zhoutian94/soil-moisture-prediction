package com.example.loran_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Date;

/**
 *  时延接口 数据协议测试
 */
@JsonTest
public class TimeOfArrivalJsonTest {

    private JacksonTester<TimeOfArrival> json;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void timeOfArrivalSerializationTest() throws IOException {
        Date date = new Date(2024 - 1900, 6, 10, 11, 48, 33);
        TimeOfArrival timeOfArrival = new TimeOfArrival(date, 3.45);
        assertThat(json.write(timeOfArrival)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(timeOfArrival)).hasJsonPathStringValue("@.createdAt");
        assertThat(json.write(timeOfArrival)).extractingJsonPathStringValue("@.createdAt")
                .isEqualTo("2024-07-10 11:48:33");

        assertThat(json.write(timeOfArrival)).hasJsonPathNumberValue("@.delay");
        assertThat(json.write(timeOfArrival)).extractingJsonPathNumberValue("@.delay")
                .isEqualTo(3.45);
    }
}