package com.example.loran_service.util;



import java.text.NumberFormat;
import java.util.List;

/**
 * 算法工具类
 */
public class AlgorithmUtil {

    /**
     * 地球平均半径（单位：米）
     */
    private static final double EARTH_AVG_RADIUS = 6371000;

    /**
     * 经纬度转化为弧度(rad)
     *
     * @param d 经度/纬度
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 方法三：（基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多。）
     *
     * @param longitude1 第一点的经度
     * @param latitude1  第一点的纬度
     * @param longitude2 第二点的经度
     * @param latitude2  第二点的纬度
     * @return 返回的距离，单位km
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLat1 = rad(latitude1);
        double radLat2 = rad(latitude2);
        double a = radLat1 - radLat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_AVG_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return s / 1000d;
    }

    /**
     * 判断坐标点是否在圆形区域内
     * 计算这个坐标点和圆心点之间的距离，然后跟圆的半径进行比较，如果比半径大，就不在圆形区域内，如果小于等于圆的半径，则该坐标点在圆形区域内
     *
     * @param longitude1 第一点的经度
     * @param latitude1  第一点的纬度
     * @param longitude2 第二点的经度
     * @param latitude2  第二点的纬度
     * @param radius     圆形范围半径（单位：千米）
     * @return true：在区域内; false:不在区域内
     */
    public static boolean isInCircle(double longitude1, double latitude1, double longitude2, double latitude2, Double radius) {
        return getDistance(longitude1, latitude1, longitude2, latitude2) <= radius;
    }

    /**
     * 计算相关系数
     *
     * @param x 样本1
     * @param y 样本2
     * @return 相关系数
     */
    public static Double calcCoefficient(List<Double> x, List<Double> y) {
        if (x.isEmpty() || y.isEmpty() || x.size() != y.size()) {
            return null;
        }
        double size = x.size();
        double xSum = 0d;
        double ySum = 0d;
        // 计算和
        for (int i = 0; i < size; i++) {
            xSum += x.get(i);
            ySum += y.get(i);
        }
        // 计算均值
        double xAvg = xSum / size;
        double yAvg = ySum / size;
        // 方差
        double xVariance = 0.0d;
        double yVariance = 0.0d;
        // 协方差
        double covariance = 0.0d;

        for (int i = 0; i < size; i++) {
            covariance += (x.get(i) - xAvg) * (y.get(i) - yAvg);
            xVariance += Math.pow((x.get(i) - xAvg), 2);
            yVariance += Math.pow((y.get(i) - yAvg), 2);
        }

        double sqrtXYVariance = Math.sqrt(xVariance * yVariance);
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumIntegerDigits(3);
        double result = sqrtXYVariance == 0.0d ? 1.0d : covariance / sqrtXYVariance;
        return Double.valueOf(instance.format(result));
    }


}