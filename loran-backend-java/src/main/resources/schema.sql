DROP TABLE IF EXISTS drs_info;
CREATE TABLE drs_info
(
    `id` BIGINT COMMENT '差分基准站ID',
    `name` VARCHAR(255) COMMENT '差分基准站名',
    `ipv4` VARCHAR(255) COMMENT '差分基准站IP地址',
    `province` VARCHAR(255) COMMENT '所属省',
    `time` BIGINT COMMENT '当前基准站信息创建时间',
    `longitude` REAL COMMENT '经度',
    `latitude` REAL COMMENT '纬度',
    `ipv4mask` VARCHAR(255) COMMENT '子网掩码',
    `height` REAL COMMENT '海拔',
    `type` INT COMMENT '站点类型，0是差分站，1是发播台'
);

DROP TABLE IF EXISTS drs_dmd;

CREATE TABLE drs_dmd
(
    `station_id` BIGINT COMMENT '基准站ID，范围1-1024',
    `transmitting_station_id` BIGINT COMMENT '发播台ID，范围1-16',
    `receiver_id` BIGINT COMMENT '接收机ID，范围1-2',
    `channel_id` BIGINT COMMENT '通道编号，范围1-3',
    `time` BIGINT COMMENT '采集时间，Unix时间',
    `differential_delay` DOUBLE null COMMENT '时延测量数据',
    `station_name` VARCHAR(255) COMMENT '差分站名称',
    `province` VARCHAR(255) COMMENT '所属省'
)