package com.lxyg.app.business.utils;

public class DUtil {
//	private static double pi = 3.14159265358979324;  
//	private static double a = 6378245.0;  
//	private static double ee = 0.00669342162296594323;  
	private static  double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	public static double []  huoxingToBaidu(double lat,double lng){
		double p[] = new double[2];
		double x = lng,y = lat;
		double z = Math.sqrt(x*x+y*y)+0.0002*Math.sin(y*x_pi);
		double theta = Math.atan2(y, x)+0.000003*Math.cos(x*x_pi);
		p[1] = z*Math.cos(theta)+0.0065;//lng
		p[0] = z*Math.sin(theta)+0.006;//lat
		return p;
	}
}
