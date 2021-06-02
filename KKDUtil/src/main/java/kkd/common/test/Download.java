package kkd.common.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import kkd.common.util.HttpUtil;


public class Download {
	public static void main(String[] args) {
		Integer i=1000*30;
		Map<String,String> p=new HashMap<String, String>();
		HttpUtil.downloadFile("http://dlsw.baidu.com/sw-search-sp/soft/b3/19971/CallofDuty_1.0.1404208018.rar", p, i, new File("E:/a.rar"));
	}
}
