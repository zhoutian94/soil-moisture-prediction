package com.example.loran_service;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 时延数据
 */
@Data
@NoArgsConstructor
public class TimeOfArrival {

    public TimeOfArrival(Date date, double delay) {
        this.createdAt = date;
        this.delay = delay;
    }

    /**
     * 数据创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    /**
     *时延拟合值
     */
    private double delay;
}
