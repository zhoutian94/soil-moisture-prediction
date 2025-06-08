package com.example.loran_service;

import com.example.loran_service.service.LoranService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/api/toa")
@RestController
public class LoranServiceController {

    @Resource
    private LoranService loranService;

    @PostMapping
    public TimeOfArrival getTOA(@RequestBody TOARequest request) {
        return loranService.getTOA(request.getLat(), request.getLon());
    }
}
