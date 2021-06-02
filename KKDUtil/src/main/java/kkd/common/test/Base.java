package kkd.common.test;


import java.util.Stack;

import kkd.common.dao.dbuitl.BaseDataSourceProviderImpl;
import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.dao.dbuitl.JDBC.MyBack;
import kkd.common.util.FileUtil;
import kkd.common.util.Md5Util;


public class Base {
	/**
	 * 将数转为任意进制
	 * @param num
	 * @param base
	 * @return
	 */
	public static String baseString(int num,int base){
		if(base > 100){
			throw new RuntimeException("进制数超出范围，base<=16");
		}
		StringBuffer str = new StringBuffer("");
		String digths = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Stack<Character> s = new Stack<Character>();
		while(num != 0){
			s.push(digths.charAt(num%base));
			num/=base;
		}
		while(!s.isEmpty()){
			str.append(s.pop());
		}
		return str.toString();
	}
	/**
	 * 16进制内任意进制转换
	 * @param num
	 * @param srcBase
	 * @param destBase
	 * @return
	 */
	public static String baseNum(String num,int srcBase,int destBase){
		if(srcBase == destBase){
			return num;
		}
		String digths = "0123456789ABCDEF";
		char[] chars = num.toCharArray();
		int len = chars.length;
		if(destBase != 10){//目标进制不是十进制 先转化为十进制
			num = baseNum(num,srcBase,10);
		}else{
			int n = 0;
			for(int i = len - 1; i >=0; i--){
				n+=digths.indexOf(chars[i])*Math.pow(srcBase, len - i - 1);
			}
			return n + "";
		}
		return baseString(Integer.valueOf(num),destBase);
	}
	
	public static void main(String[] args) {
		JDBC.setDefultDataSourceProvider(BaseDataSourceProviderImpl.getInstance(), "test");
		JDBC jdbc=new JDBC();
		jdbc.execute(new MyBack() {
			int i=1;
			Base b=new Base();
			@Override
			public Object exe(DbHelper dh) throws Exception {
				while(true){
					StringBuilder sb=new StringBuilder("insert into md5 (minwen,md5)values(");
					sb.append("'");
					String bb=b.baseString(i++, 62);
					String c=Md5Util.encode(bb);
					sb.append(bb);
					sb.append("','");
					sb.append(c);
					sb.append("')");
					dh.executeUpdate(sb.toString(),null);
					if(i%100000==0){
						dh.commit();
						System.out.println(i);
					}
				}
			}
		});
//			sb.append(bb+"\t"+c+"\r\n");
//			if(sb.length()>1024*1024*10){
//				FileUtil.appendMethodB("E:/a.txt", sb.toString());
//				System.out.println(i);
//				sb=new StringBuilder();
//			}
			
		
		  
	}
	private static void c() {
		Base b=new Base();
		int i=2085087807;
		StringBuilder sb=new StringBuilder();
		while(true){
			String bb=b.baseString(i++, 62);
			String c=Md5Util.encode(bb);
			sb.append(bb+"\t"+c+"\r\n");
			if(sb.length()>1024*1024*10){
				FileUtil.appendMethodB("E:/a.txt", sb.toString());
				System.out.println(i);
				sb=new StringBuilder();
			}
			
		}
	}
}
