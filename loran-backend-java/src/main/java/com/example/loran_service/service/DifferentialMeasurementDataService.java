package com.example.loran_service.service;

import com.example.loran_service.entity.DifferentialMeasurementData;

import java.util.List;


/**
* @author 刘世浩
* @description 针对表【differential_measurement_data(differential measurement data差分测量数据表)】的数据库操作Service
* @createDate 2023-07-29 18:44:55
*/
public interface DifferentialMeasurementDataService  {



    /**
    * @description 根据基准站id查询最新数据
    * @param stationId 基准站ID
    */
    List<DifferentialMeasurementData> getLastDataList(Integer stationId);

}
