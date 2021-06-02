package kkd.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import kkd.common.logger.LogWriter;

/**
 * 系统间调用
 * @author tanbin
 *
 */
public class HttpInvoke {
	private Map<String,String> map=new HashMap<String, String>();//能访问当前项目的 app配置
//	private Map<String,String> vmap=new HashMap<String, String>();//访问其他项目的配置
	
	

	/**
	 * 设置哪些应用能访问当前项目
	 * @param app
	 * @param pwd
	 */
//	public  void putVisitMe(String app,String pwd){
//		map.put(app, pwd);
//	}

		
	/**
	 * 设置访问其他项目的密钥
	 * @param app
	 * @param pwd
	 */
//	public void putVisitOther(String app,String pwd){
//		vmap.put(app, pwd);
//	}
	
	public void putVisit(String app,String pwd){
		map.put(app, pwd);
	}
	/**
	 * 系统之间调用
	 * @param appId
	 * @param url
	 * @param params
	 * @return
	 */
	public String invoke(String appId,String url,Map<String,String> params){
		Map<String,String> head=new HashMap<String, String>();
		String data=createPost(appId,map.get(appId),params,head);
		LogWriter.debug("invoke请求URL： " + url);
		LogWriter.debug("invoke请求参数： " + data);
		return HttpUtil.httpSendBase(url, data, null, head);
	}
	
	public <T> T invoke(String appId,String url,Map<String,String> params,TypeReference<T> type){
		Map<String,String> head=new HashMap<String, String>();
		String data=createPost(appId,map.get(appId),params,head);
		LogWriter.debug("invoke请求URL： " + url);
		LogWriter.debug("invoke请求参数： " + data);
		String msg= HttpUtil.httpSendBase(url, data, null, head);
		return JSON.parseObject(msg, type);
	}
	/**
	 * 系统之间调用
	 * @param appId
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public Object invokeWithObj(String appId,String url,Map<String,String> params) throws IOException, ClassNotFoundException{
		Map<String,String> head=new HashMap<String, String>();
		String data=createPost(appId,map.get(appId),params,head);
		
		LogWriter.debug("invokeWithObj请求URL： " + url);
		LogWriter.debug("invokeWithObj请求参数： " + data);
		String rs1= HttpUtil.httpSendBase(url, data, null, head);
//		ByteArrayInputStream byteIn = new ByteArrayInputStream(rs1.getBytes());   
//		ObjectInputStream in = new ObjectInputStream(byteIn);
//		return in.readObject(); //读对象，反序列化  
		
		rs1=URLDecoder.decode(rs1,"UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rs1.getBytes("ISO-8859-1"));
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);  
		return objectInputStream.readObject();
		
		
	}
	
	/**
	 * 序列化对象
	 * @param obj
	 * @throws IOException
	 */
	public String serializable(Object obj) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(obj);//写对象，序列化  
		out.flush();
		out.close();
		return URLEncoder.encode(byteOut.toString("ISO-8859-1"), "UTF-8");
	}
	
	/**
	 * 检查请求合法性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkRequest(HttpServletRequest req){
		String appId=req.getHeader("appId");
		String sign=req.getHeader("sign");
		String signDate=req.getHeader("signDate");
		Map<String,String[]> args=req.getParameterMap();
		boolean b=checkSign(appId, sign,signDate, args);
		if(!b){
			LogWriter.debug("签名:"+b);
		}
		return b;
	}
	
	private boolean checkSign(String appId, String sign,String signDate,
			Map<String, String[]> params) {
		SortedMap<String,String> map1=new TreeMap<String, String>();
		for (Map.Entry<String,String[]> e  : params.entrySet()) {
			String[] vs=(String[])e.getValue();
			map1.put(e.getKey(), vs[0]);
		}
		StringBuilder sb=new StringBuilder();
		StringBuilder args=new StringBuilder();
		args.append("pwd=");
		args.append(map.get(appId));
		args.append("_");
		args.append(signDate);
		for (Map.Entry<String,String> e  : map1.entrySet()) {
			String key=e.getKey();
			String value=e.getValue();
			sb.append("&");
			sb.append(key);
			sb.append("=");
			sb.append(value);
		}
		System.out.println("md5:"+args.append(sb));
		String oldSign=Md5Util.encode(args.append(sb).toString());
		if(StringUtil.isEqual(oldSign, sign)){
			return true;
		}
		return false;
	}
	
	/**
	 * 创建签名
	 * @param appId
	 * @param pwd
	 * @param params
	 * @return
	 */
	private String createPost(String appId, String pwd,
			Map<String, String> params,Map<String, String> head) {
		SortedMap<String,String> map=new TreeMap<String, String>();
		for (Map.Entry<String,String> e  : params.entrySet()) {
			map.put(e.getKey(), e.getValue());
		}
		StringBuilder sb=new StringBuilder();
		StringBuilder args=new StringBuilder();
		args.append("pwd=");
		args.append(pwd);
		args.append("_");
		long currentTimeMillis=System.currentTimeMillis();
		args.append(currentTimeMillis);
		for (Map.Entry<String,String> e  : map.entrySet()) {
			String key=e.getKey();
			String value=e.getValue();
			sb.append("&");
			sb.append(key);
			sb.append("=");
			if(value!=null){
				sb.append(value);
			}
		}
		System.out.println("md51:"+args.append(sb));
		String sign=Md5Util.encode(args.append(sb).toString());
		head.put("appId", appId);
		head.put("sign", sign);
		head.put("signDate", currentTimeMillis+"");
		return sb.toString();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		HttpInvoke h=new HttpInvoke();
//		h.putVisitMe("UserWeb", "123456");
//		h.putVisitOther("UserWeb", "123456");
//		Map<String,String>  params=new HashMap<String, String>();
//		params.put("a", "b");
//		h.invoke("UserWeb", "http://127.0.0.1:8080/ResourceWeb/test.jsp", params);
//		
////		h.checkRequest(req);
		
	}
}
