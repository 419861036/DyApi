package kkd.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EnvUtil {
	public static final String projectName="kkd.common.util.EnvUtil.projectName";//项目名
	public static final String webServerPath="kkd.common.util.EnvUtil.webServerPath";//web服务器路径
	public static final String projectConfigPath="kkd.common.util.EnvUtil.projectConfigPath";//项目配置路径
	public static final String projectStateConfig="kkd.common.util.EnvUtil.projectStateConfig";//项目状态配置
	private static Map<String,String> pConfig = new HashMap<String, String>();
	
	public static String getServerPath(){
		String serverPath=System.getProperty("catalina.home");
		if(serverPath==null){
			serverPath=System.getProperty("jetty.home");
		}
		return serverPath;
	}
	/**
	 * 存放系统环境变量
	 * @param key
	 * @param value
	 */
	public static  void put(String key,String value) {
		pConfig.put(key, value);
	}
	
	/**
	 * 获取环境变量
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return pConfig.get(key);
	}

	
	private static String getHostAddress() {
        String address = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> ads = ni.getInetAddresses();
                while (ads.hasMoreElements()) {
                    InetAddress ip = ads.nextElement();
                    if (!ip.isLoopbackAddress() && ip.isSiteLocalAddress()) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch (Exception e) {
        }
        return address;
    }
	
	public static List<String> getLocalInetAddress() {
        List<String> inetAddressList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> addrs = networkInterface.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    inetAddressList.add(addrs.nextElement().getHostAddress());
                }
            }
        }
        catch (SocketException e) {
            throw new RuntimeException("get local inet address fail", e);
        }

        return inetAddressList;
    }


    
	 public static int getPid() {
	        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
	        String name = runtime.getName(); // format: "pid@hostname"
	        try {
	            return Integer.parseInt(name.substring(0, name.indexOf('@')));
	        }
	        catch (Exception e) {
	            return -1;
	        }
	 }
	 
	 public static String currentStackTrace() {
	        StringBuilder sb = new StringBuilder();
	        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	        for (StackTraceElement ste : stackTrace) {
	            sb.append("\n\t");
	            sb.append(ste.toString());
	        }

	        return sb.toString();
	 }
	 
	 
	 public static String jstack() {
	        StringBuilder result = new StringBuilder();
	        try {
	            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
	            Iterator<Map.Entry<Thread, StackTraceElement[]>> ite = map.entrySet().iterator();
	            while (ite.hasNext()) {
	                Map.Entry<Thread, StackTraceElement[]> entry = ite.next();
	                StackTraceElement[] elements = entry.getValue();
	                Thread thread = entry.getKey();
	                if (elements != null && elements.length > 0) {
	                    String threadName = entry.getKey().getName();
	                    result.append(String.format("%-40sTID: %d STATE: %s\n", threadName, thread.getId(),
	                        thread.getState()));
	                    for (StackTraceElement el : elements) {
	                        result.append(String.format("%-40s%s\n", threadName, el.toString()));
	                    }
	                    result.append("\n");
	                }
	            }
	        }
	        catch (Throwable e) {
//	            result.append(RemotingHelper.exceptionSimpleDesc(e));
	        }

	        return result.toString();
	    }
}
