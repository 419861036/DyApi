package kkd.common.util;

public class HarvenSinUtil {
	public static double EARTH_RADIUS = 6371.0;//km 地球半径 平均值，千米
	
	public static void main(String[] args) {
		//39.94607,116.32793  31.24063,121.42575
		double d=Distance( 116.384639,39.914036, 116.4188,39.916406 );
		System.out.println(d);
	}
	
	public static double HaverSin(double theta)
	{
		double v = Math.sin(theta / 2);
		return v * v;
	}

	public static double Distance(double lat1,double lon1, double lat2,double lon2){
		//用haversine公式计算球面两点间的距离。
		//经纬度转换成弧度
		lat1 = ConvertDegreesToRadians(lat1);
		lon1 = ConvertDegreesToRadians(lon1);
		lat2 = ConvertDegreesToRadians(lat2);
		lon2 = ConvertDegreesToRadians(lon2);
		//差值
		double vLon = Math.abs(lon1 - lon2);
		double vLat = Math.abs(lat1 - lat2);
		//h is the great circle distance in radians, great circle就是一个球体上的切面，它的圆心即是球心的一个周长最大的圆。
		double h = HaverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * HaverSin(vLon);
		double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));
		return distance;
	}

	public static double ConvertDegreesToRadians(double degrees){
		return degrees * Math.PI / 180;
	}
	public static double ConvertRadiansToDegrees(double radian){
		return radian * 180.0 / Math.PI;
	}
}
