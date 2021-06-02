package kkd.common.util.exp;

import java.util.HashMap;
import java.util.Map;

import kkd.common.util.StringUtil;


public class ExpHandle {
	public static void main(String[] args) {
//		String s="${LjTime('s',24)} >#{ph} and ${CxTime('avg','xxx' ,'f') }>#{ph}";
//		String s="(${累计次数(s1,24小时)}>4 or ${累计时间(s1,24小时)}>2 ${持续时间(s1,24小时)}>2 or ${持续次数(s1,24小时)}>2) and (${累计次数(s2,24小时)}>4 or ${累计时间(s2,24小时)}>2 ${持续时间(s2,24小时)}>2 or ${持续次数(s2,24小时)}>2)";
		String s="${ph}>3 and ${ph }>4 and ${LjTime('s' ,24)}>4";
		test1(s);
		
//		test2(s);
		
//		ExprSupport e=new ExprSupport();
//		e.parseExpr(s);
	}
	private static void test2(String s) {
		Formatter f=new Formatter();
		f.setPutCodeInfo(true);
		f.setFormatStartKey("#{");
		StringBuilder sb=new StringBuilder();
		f.parse(s, sb, null);
		System.out.println(sb);
		Map<Integer,CodeInfo> map=f.getCodeInfoMap();
		
		for (Map.Entry<Integer, CodeInfo> e : map.entrySet()) {
			System.out.println(e.getKey());
//			String s="a('a',123 )";
			String s1=e.getValue().getKey();
			int start=e.getValue().getStartIndex();
			int end=e.getValue().getEndIndex();
			String ss=s.substring(start,end);
			System.out.println(ss);
		}
	}
	private static void test1(String s) {
		Formatter f=new Formatter();
		f.setPutCodeInfo(true);
		f.setFormatStartKey("${");
		StringBuilder sb=new StringBuilder();
		f.parse(s, sb, null);
		System.out.println(sb);
		Map<Integer,String> replaceMap=new HashMap<>();
		replaceMap.put(0, "a");
		replaceMap.put(1, "b");
		replaceMap.put(2, "c");
		String ss=f.updateFormat(s, f.getCodeInfoMap(), replaceMap);
		System.out.println(ss);
		
//		for (Map.Entry<Integer, CodeInfo> e : map.entrySet()) {
////			System.out.println(e.getKey());
////			String s="a('a',123 )";
//			String s1=e.getValue().getKey();
//			int start=e.getValue().getStartIndex();
//			int end=e.getValue().getEndIndex();
//			String ss=s.substring(start,end);
//			System.out.println(ss);
////			parseMethod(ss);
//		}
	}

	public static void parseMethod(String methodStr) {
		int startKh=methodStr.indexOf("(");
		int endKh=methodStr.indexOf(")",startKh);
		String methodName=methodStr.substring(0,startKh);
		System.out.println(methodName);
		String argsStr=methodStr.substring(startKh+1,endKh);
		String[] args=StringUtil.split(argsStr, ",");
		for (String string : args) {
			System.out.println(string);
		}
	}
}
