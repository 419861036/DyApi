package kkd.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalcUtil {
	 private static final int DEF_DIV_SCALE = 2;  
	 
//	 public static void main(String[] args) {
//		 System.out.println(add(0.05, 0.01));
//		 double a=0.05+0.01;
//		 System.out.println(a);
//		 System.out.println(0.06);
//		 System.out.println(0.05+0.01);  
//		 System.out.println(1.0-0.42);  
//		 System.out.println(4.015*100);  
//		 System.out.println(123.3/100); 
//	}
	 
	 public static void main(String[] args) throws UnsupportedEncodingException {
//			WXUserInfoBo bo = new WXUserInfoBo();
//			WXUserInfoVo info =  new WXUserInfoVo();
//			info.setUnionid("dadaa");
//			bo.syncWXUserInfo(info);
//			Helper h=new Helper();
//			System.out.println(h.DESEncode("a"));
//			String s=h.DESDecode("FBD3619C4386B9189E0192CC9F644AE9BB1155054747A6282518D9C7C8C6090A9878A62B593AFE29547A550386D7AC0B30A094C1C08AC6FE5CC888E6AB58CAE58EF57023BD503A49DEF006FABE7671230A8C9A0F00673C4C3464345B5D1F8819C8B5D040C7F0FED698D86F82C152193806D95556E810BC8637271A4ED886C660");
//			System.out.println(URLDecoder.decode(s, "UTF-8"));
//			0.01=00000010100011110101110000101
//			0.05=0000110011001100110011001101
//			0.06=0000111101011100001010001111
//			Double a=0.06;
//			Integer[] i= toBin(a);
//			for (int j = 0; j < i.length; j++) {
//				System.out.print(i[j]);
//			}
//		 	System.out.println(0.5+0.1);
		 	long a=Double.doubleToLongBits(0.6);
		 	for (int i = 0; i < 51; i++) {
				long b=a>>i;
		 		b=b^0x000fffffffffffffL;
		 		System.out.println(b);
			}
//		 	System.out.println();
		}
	 
		public static   Integer[] toBin(Double f) {
			if (f >= 1 || f <= 0)
				return null;
			List<Integer> list = new ArrayList<Integer>();
			Set<Double> set = new HashSet<Double>();
			int MAX = 800; // 最多8位

			int bits = 0;
			while (true) {
				f = calc(f, set, list);
				bits++;
				if (f == -1 || bits >= MAX){
					break;
				}
					
			}

			return (Integer[])list.toArray(new Integer[0]);
		}
		
		private static Double calc(Double f, Set<Double> set, List<Integer> list) {
			if (f == 0 || set.contains(f))
				return -1.0;
			Double t=CalcUtil.mul(f, 2.0);
			if (t >= 1) {
				list.add(1);
				return CalcUtil.sub(t, 1.0);
			} else {
				list.add(0);
				return t;
			}
		}
	/** 
     * * 两个Double数相加 * 
     *  
     * @param v1 * 
     * @param v2 * 
     * @return Double 
     */  
    public static Double add(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.add(b2).doubleValue());  
    }  
  
    /** 
     * * 两个Double数相减 * 
     *  
     * @param v1 * 
     * @param v2 * 
     * @return Double 
     */  
    public static Double sub(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.subtract(b2).doubleValue());  
    }  
  
    /** 
     * * 两个Double数相乘 * 
     *  
     * @param v1 * 
     * @param v2 * 
     * @return Double 
     */  
    public static Double mul(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.multiply(b2).doubleValue());  
    }  
  
    /** 
     * * 两个Double数相除 * 
     *  
     * @param v1 * 
     * @param v2 * 
     * @return Double 
     */  
    public static Double div(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1  
                .divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)  
                .doubleValue());  
    }  
    
    public Double div(double d1,double d2,int scale){
        //  当然在此之前，你要判断分母是否为0，   
        //  为0你可以根据实际需求做相应的处理

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide 
               (bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    } 
  
    /** 
     * * 两个Double数相除，并保留scale位小数 * 
     *  
     * @param v1 * 
     * @param v2 * 
     * @param scale * 
     * @return Double 
     */  
    public static Double div(Double v1, Double v2, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException(  
                    "The scale must be a positive integer or zero");  
        }  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)  
                .doubleValue());  
    }  
      
    /** 
     * @param v1 
     * @return 返回指定Double的负数 
     */  
    public static Double neg(Double v1) {  
        return sub(new Double(0),v1);  
    } 
}
