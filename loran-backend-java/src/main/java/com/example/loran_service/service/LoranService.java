package com.example.loran_service.service;

import com.example.loran_service.TimeOfArrival;

public interface LoranService {

    /**
     * 获取指定坐标的时延拟合值
     * @param lat
     * @param lon
     * @return
     */
    TimeOfArrival getTOA(double lat, double lon);
}
