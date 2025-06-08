package com.example.loran_service.service.impl;

import com.example.loran_service.TimeOfArrival;
import com.example.loran_service.entity.DifferentialMeasurementData;
import com.example.loran_service.entity.DrsInfo;
import com.example.loran_service.service.DifferentialMeasurementDataService;
import com.example.loran_service.service.DrsInfoService;
import com.example.loran_service.service.LoranService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoranServiceImpl implements LoranService {

    private final DrsInfoService drsInfoService;
    private final DifferentialMeasurementDataService differentialMeasurementDataService;

    public LoranServiceImpl(DrsInfoService drsInfoService, DifferentialMeasurementDataService differentialMeasurementDataService) {
        this.drsInfoService = drsInfoService;
        this.differentialMeasurementDataService = differentialMeasurementDataService;
    }

    /**
     * 获取指定坐标的时延拟合值
     *
     * @param lat
     * @param lon
     * @return
     */
    @Override
    public TimeOfArrival getTOA(double lat, double lon) {
//        1·确定覆盖目标坐标的差分站集合（为实现100ns指标，陆地上差分站的最大作用范围不大于58km）
        List<DrsInfo> drsList = drsInfoService.getDrsInfoListByDistance(lat, lon, 58);
        List<DrsInfo> stationsDrsList = drsList.stream().filter(drsInfo -> drsInfo.getType() == 0).collect(Collectors.toList());
//        2·获取这些差分站的时延数据
        Map<String, Double> avgDelayMap = new HashMap<>();
        for (DrsInfo drsInfo : stationsDrsList) {
            List<DifferentialMeasurementData> delayData = differentialMeasurementDataService.getLastDataList(drsInfo.getId());
            // 按照通道号对时延数据进行算术平均
            Map<Integer, Double> averageDelayByChannel = delayData.stream()
                    .collect(Collectors.groupingBy(DifferentialMeasurementData::getChannelId,
                            Collectors.averagingDouble(DifferentialMeasurementData::getDifferentialDelay)));
            double averageDelay = averageDelayByChannel.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            avgDelayMap.put(drsInfo.getId().toString(), averageDelay);
        }
//        3·计算这些差分站的权重
//       3.1 计算每个差分站的距离
        Map<String, Double> stationDistanceReciprocalSquareMap = new HashMap<>();
        Map<String, Double> stationWeigthMap = new HashMap<>();
        double sumReciprocalSquare = 0.0;
        for (DrsInfo drsInfo : drsList) {
            //计算欧氏距离
            double distance = Math.sqrt(Math.pow(drsInfo.getLatitude() - lat, 2) + Math.pow(drsInfo.getLongitude() - lon, 2));
            double distanceReciprocalSquare = 1 / (distance * distance);
            stationDistanceReciprocalSquareMap.put(drsInfo.getId().toString(), distanceReciprocalSquare);
            sumReciprocalSquare += distanceReciprocalSquare;
        }
//        3.2 计算每个差分站的权重
        for (DrsInfo drsInfo : drsList) {
            double weigth = stationDistanceReciprocalSquareMap.get(drsInfo.getId().toString()) / sumReciprocalSquare;
            stationWeigthMap.put(drsInfo.getId().toString(), weigth);
        }

//        4·计算目标坐标的时延拟合值
        double weightedAverage = 0.0;
        for (Map.Entry<String, Double> avgMapEntry : avgDelayMap.entrySet()) {
            Double weigth = stationWeigthMap.get(avgMapEntry.getKey());
            weightedAverage +=  weigth * avgMapEntry.getValue();
        }
        return new TimeOfArrival(new Date(), weightedAverage);
    }

}
