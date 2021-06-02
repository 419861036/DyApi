package kkd.common.util;

import java.awt.Point;

public class GeoUtils {
	
	 /**
     * 计算两经纬度点之间的距离（单位：米）
     * @param lng1  经度
     * @param lat1  纬度
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1,double lat1,double lng2,double lat2){
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    
    /**
     * 计算TP值
     * @param curPoint      当前点
     * @param relatedPoint  偏移点
     * @param isGeography   是否是地理坐标 false为2d坐标
     * @return              tp值
     */
    public static double getDirAngle(Point curPoint,Point relatedPoint,boolean isGeography){
        double result = 0;
        double relatedPointLat=39.914036;
        double curPointLat=39.916406;
        double relatedPointLng=116.384639;
        double curPointLng=116.4188;
        if(isGeography){
            double y2 = Math.toRadians(relatedPointLat);
            double y1 = Math.toRadians(curPointLat);
            double alpha = Math.atan2(relatedPointLat - curPointLat, (relatedPointLng - curPointLng) * Math.cos((y2 - y1) / 2));//纬度方向乘以cos(y2-y1/2)
            double delta =alpha<0?(2*Math.PI+alpha):alpha;
            result = Math.toDegrees(delta);
        }else {
            double alpha = Math.atan2(relatedPointLat - curPointLat, relatedPointLng - curPointLng);
            double delta=alpha<0?(2*Math.PI+alpha):alpha;
            result = Math.toDegrees(delta);
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println(getDistance(116.384639,39.914036,116.4188,39.916406  ));
//        System.out.println(getDistance(121.446014,31.215937,121.446028464238,31.2158502442799  ));
    }
}
