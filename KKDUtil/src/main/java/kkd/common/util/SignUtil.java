package kkd.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import kkd.common.util.Md5Util;

public class SignUtil {
	
	@SuppressWarnings("rawtypes")
	public static String createMd5Sign(SortedMap<String,String> signParams){
		StringBuilder sb = new StringBuilder();
		Set<?> es = signParams.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
			//要采用URLENCODER的原始值！
		}
		String params = sb.substring(0, sb.lastIndexOf("&"));
		System.out.println("md5 sb:" + params);
		return Md5Util.encode(params);
	}
	
	public static void main(String[] args) {
		SortedMap<String,String> signParams=new TreeMap<String, String>();
		signParams.put("a", "data");
		signParams.put("b", "data");
		String s=createMd5Sign(signParams);
		System.out.println(s);
	}
}
