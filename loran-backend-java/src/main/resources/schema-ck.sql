
CREATE TABLE drs_info
(

    `id` Int64 COMMENT '差分基准站ID',

    `name` String COMMENT '差分基准站名',

    `ipv4` String COMMENT '差分基准站IP地址',

    `province` String COMMENT '所属省',

    `time` Nullable(Int64) COMMENT '当前基准站信息创建时间',

    `longitude` Nullable(Float32) COMMENT '经度',

    `latitude` Nullable(Float32) COMMENT '纬度',

    `ipv4mask` Nullable(String) COMMENT '子网掩码',

    `height` Nullable(Float32) COMMENT '海拔',

    `type` Nullable(Int32) COMMENT '站点类型，0是差分站，1是发播台'
)
ENGINE = MergeTree
ORDER BY id
SETTINGS index_granularity = 8192
COMMENT '差分基准站信息表';