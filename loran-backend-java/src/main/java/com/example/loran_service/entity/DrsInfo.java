package com.example.loran_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * drs_info差分基准站信息表，drs指differential reference station 差分基准站
 * @TableName drs_info
 */
@TableName(value ="drs_info")
@Data
public class DrsInfo implements Serializable {
    /**
     * 差分基准站ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 差分基准站名
     */
    private String name;

    /**
     * 差分基准站IP地址
     */
    private String ipv4;

    /**
     * 所属省
     */
    private String province;

    /**
     * 当前基准站信息创建时间
     */
    private Long time;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 子网掩码
     */
    private String ipv4mask;

    /**
     * 海拔
     */
    private Float height;

    /**
     * 类型（0是差分站，1是发播台）
     */
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
