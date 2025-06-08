package com.example.loran_service.service;

import com.example.loran_service.entity.DrsInfo;

import java.util.List;

public interface DrsInfoService {
    public List<DrsInfo> getDrsInfoListByDistance(double lat, double lon, int distance);
}
