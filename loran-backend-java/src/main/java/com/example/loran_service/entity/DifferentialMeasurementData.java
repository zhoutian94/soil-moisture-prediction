package com.example.loran_service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * differential measurement data差分测量数据表，保存修正前与修正后的数据
 * @TableName differential_measurement_data
 */
@TableName(value ="drs_dmd")
@Data
public class DifferentialMeasurementData implements Serializable {
    /**
     * 差分测量数据内容表
     */
//    @TableId
//    private Integer id;

    /**
     * 基准站ID
     */
    private Integer stationId;

    /**
     * 发波台ID
     */
    private Integer transmittingStationId;

    /**
     * 接收机ID
     */
    private Integer receiverId;

    private Integer channelId;

    /**
     * 采集时间
     */
    private Long time;

    /**
     * 时延测量数据
     */
    private Double differentialDelay;

    private String stationName;

    private String province;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}