package com.example.loran_service;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 时延数据请求对象
 */
@Data
@NoArgsConstructor
public class TOARequest implements Serializable {
    public final static long serialVersionUID = 1L;
    /**
     * 纬度
     */
    private double lat;
    /**
     * 经度
     */
    private double lon;

    public TOARequest(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
