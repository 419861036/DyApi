package kkd.common.util;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SequenceUtil {
	public static AtomicLong auto_increment = new AtomicLong(0); 
	
	private static DecimalFormat  df1 = new DecimalFormat("000"); 
	private AtomicLong objAutoIncrement;
	private  DecimalFormat  objDf;
	private Integer maxValue;
	public SequenceUtil(Integer initValue,Integer maxValue,String format) {
		this.objAutoIncrement=new AtomicLong(initValue);
		this.objDf=new DecimalFormat(format); 
		this.maxValue=maxValue;
	}
	/**
	 * 获取序列号 默认方式
	 * @return
	 */
	public static String getSequenceNo(){
		if(auto_increment.compareAndSet(999, 0)){
			String j = df1.format(0+1);
			return System.currentTimeMillis()+j;
		}else{
			String j = df1.format(auto_increment.getAndIncrement()%999+1);
			return System.currentTimeMillis()+j;
		}
		
	}
	/**
	 * 获取序列
	 * @return
	 */
	public  String getObjSequenceNo(){
		if(objAutoIncrement.compareAndSet(maxValue, 0)){
			String j = objDf.format(0+1);
			return System.currentTimeMillis()+j;
		}else{
			String j = objDf.format(objAutoIncrement.getAndIncrement()%maxValue+1);
			return System.currentTimeMillis()+j;
		}
	}
	
	public static void main(String[] args) {
		
		
//		
//		System.out.println(Long.MAX_VALUE);
//		Date date2 = new Date(Long.MAX_VALUE+2);
//		Date date1 = new Date(Long.MAX_VALUE+1);
//		Date date3 = new Date(Long.MAX_VALUE);
//		
//		System.out.println(DateUtil.format(date1));
//		System.out.println(DateUtil.format(date2));
//		System.out.println(DateUtil.format(date3));
//		auto_increment = new AtomicLong(Integer.MAX_VALUE); 
//		System.out.println(Math.abs(auto_increment.getAndIncrement())%999);
//		System.out.println(Math.abs(auto_increment.getAndIncrement())%999);
//		System.out.println(Math.abs(auto_increment.getAndIncrement())%999);
//		System.out.println(Math.abs(auto_increment.getAndIncrement())%999);
//		System.out.println(Math.abs(auto_increment.getAndIncrement())%999);
//		
//		
		final SequenceUtil su=new SequenceUtil(0,9999, "0000");
		final Map<String ,String > map=new ConcurrentHashMap<String, String>();
//		final CommonBean<AtomicLong> j=new CommonBean<AtomicLong>();
//		j.setValue(new AtomicLong(0));
		Runnable r=new Runnable() {
			
			@Override
			public void run() {
				
//				Map<String ,String > map=new HashMap<String, String>();
				for (int i = 0; i < 1000; i++) {
					map.put(su.getObjSequenceNo(), "");
					System.out.println(map.size());
//					Thread t=Thread.currentThread();
//					System.out.println(t.getName()+":"+su.getObjSequenceNo());
					
				}
//				System.out.println(map.size());
//				j.getValue().getAndIncrement();
			}
		};
		for (int i = 0; i <4; i++) {
			Thread t=new Thread(r,"ThreadPool-"+i);
			t.start();
		}
//		while(true){
//			if(j.getValue().get()==4){
//				System.out.println(map.size());
//				break;
//			}
//		}
//		System.out.println(map.size());
	}
	
	
}
