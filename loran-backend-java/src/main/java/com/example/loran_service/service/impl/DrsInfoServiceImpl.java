package com.example.loran_service.service.impl;

import com.example.loran_service.entity.DrsInfo;
import com.example.loran_service.mapper.DrsInfoMapper;
import com.example.loran_service.service.DrsInfoService;
import com.example.loran_service.util.AlgorithmUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrsInfoServiceImpl implements DrsInfoService {

    @Autowired
    private DrsInfoMapper drsInfoMapper;

    @Override
    public List<DrsInfo> getDrsInfoListByDistance(double lat, double lon, int distance) {
        List<DrsInfo> drsInfos = drsInfoMapper.selectList(null);
        ArrayList<DrsInfo> drsInDistance = new ArrayList<>();
        for (DrsInfo drsInfo : drsInfos) {
            double distance1 = AlgorithmUtil.getDistance(drsInfo.getLongitude(),
                    drsInfo.getLatitude(), lon, lat);
            if (distance1 <= distance) {
                drsInDistance.add(drsInfo);
            }
        }
        return drsInDistance;
    }
}
