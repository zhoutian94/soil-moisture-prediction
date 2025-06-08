package com.example.loran_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.loran_service.entity.DifferentialMeasurementData;
import com.example.loran_service.mapper.DifferentialMeasurementDataMapper;
import com.example.loran_service.service.DifferentialMeasurementDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author 刘世浩
 * @description 针对表【differential_measurement_data(differential measurement data差分测量数据表)】的数据库操作Service实现
 * @createDate 2023-07-29 18:44:55
 */
@Service
public class DifferentialMeasurementDataServiceImpl implements DifferentialMeasurementDataService {

    @Autowired
    private DifferentialMeasurementDataMapper differentialMeasurementDataMapper;

    @Override
    public List<DifferentialMeasurementData> getLastDataList(Integer stationId) {
        // 获取最新的时间
        DifferentialMeasurementData latestData = differentialMeasurementDataMapper.selectOne(new LambdaQueryWrapper<DifferentialMeasurementData>()
                .eq(DifferentialMeasurementData::getStationId, stationId)
                .orderByDesc(DifferentialMeasurementData::getTime)
                .last("LIMIT 1")
        );
        if (latestData == null) {
            return Collections.emptyList();
        }
        Long latestTime = latestData.getTime();

        // 使用最新的时间作为查询条件
        return differentialMeasurementDataMapper.selectList(new LambdaQueryWrapper<DifferentialMeasurementData>()
                .eq(DifferentialMeasurementData::getStationId, stationId)
                .eq(DifferentialMeasurementData::getTime, latestTime)
        );
    }



}




