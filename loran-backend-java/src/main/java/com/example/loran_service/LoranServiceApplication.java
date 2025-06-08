package com.example.loran_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.loran_service.mapper")
@SpringBootApplication
public class LoranServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoranServiceApplication.class, args);
    }

}
