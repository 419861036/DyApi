package kkd.common.cache.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import kkd.common.logger.LogWriter;
import kkd.common.util.HttpUtil;
import kkd.common.util.StringUtil;

/**
 * 服务器节点
 * @author tanbin
 *
 */
public class ServerNode {
	private static int net_timeout=15;
	private static boolean debug=false;
	private static String GET_URL="/cache/get.do";
	private static String PUT_URL="/cache/put.do";
	private static String REMOVE_URL="/cache/remove.do";
	private String domain;
	private int port;
	private String path;
	private String type; 
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return domain+":"+port;
	}
	
	public Object get(String key){
		Map<String,String> params=new HashMap<String, String>();
		params.put("key",key);
		params.put("type",type);
		String tmp=HttpUtil.post(getUrl(), params,net_timeout,debug);
		try {
			if(StringUtil.isEmpty(tmp)){
				return null;
			}
//			tmp=java.net.URLDecoder.decode(tmp, "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);  
			return objectInputStream.readObject();
		} catch (UnsupportedEncodingException e) {
			LogWriter.error("",e);
		} catch (IOException e) {
			LogWriter.error("",e);
		} catch (ClassNotFoundException e) {
			LogWriter.error("",e);
		}
       return null;
	}
	
	public Object get(String key,Boolean upDate){
		Map<String,String> params=new HashMap<String, String>();
		params.put("key",key);
		params.put("type",type);
		if(upDate!=null && upDate){
			params.put("upDate","TRUE");
		}
		String tmp=HttpUtil.post(getUrl(), params,net_timeout,debug);
		try {
			if(StringUtil.isEmpty(tmp)){
				return null;
			}
//			tmp=java.net.URLDecoder.decode(tmp, "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);  
			return objectInputStream.readObject();
		} catch (UnsupportedEncodingException e) {
			LogWriter.error("",e);
		} catch (IOException e) {
			LogWriter.error("",e);
		} catch (ClassNotFoundException e) {
			LogWriter.error("",e);
		}
       return null;
	}
	
	public String get(String key,boolean innser,String args,Boolean upDate){
		Map<String,String> params=new HashMap<String, String>();
		params.put("key",key);
		params.put("type",type);
		if(innser){
			params.put("inner","TRUE");
			params.put("innerArg",args);
		}
		if(upDate!=null && upDate){
			params.put("upDate","TRUE");
		}
		String tmp=HttpUtil.post(getUrl(), params,net_timeout,debug);
		if(StringUtil.isEmpty(tmp)){
			return null;
		}
		return tmp;
	}
	
	public void put(String key,long timeout,Object value){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		ObjectOutputStream objectOutputStream = null;  
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	        objectOutputStream.writeObject(value);
	        String tmp=byteArrayOutputStream.toString("ISO-8859-1");
	        tmp=java.net.URLEncoder.encode(tmp, "UTF-8");  
	    	Map<String,String> params=new HashMap<String, String>();
			params.put("key",key);
			params.put("value",tmp);
			params.put("type",type);
			params.put("timeout",timeout+"");
			 HttpUtil.post(putUrl(), params,net_timeout,debug);
		} catch (Exception e) {
			LogWriter.debug("put cache exception", e);
		}finally{
			try {
				objectOutputStream.close();
			} catch (IOException e) {
			}  
	        try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
			}  
		}
	}
	
	public void remove(String key){
		Map<String,String> params=new HashMap<String, String>();
		params.put("key",key);
		params.put("type",type);
		HttpUtil.post(removeUrl(), params,net_timeout,debug);
	}
	
	public String getUrl(){
		String path1=path==null?"":path;
		return domain+":"+port+path1+GET_URL;
	}
	
	public String removeUrl(){
		String path1=path==null?"":path;
		return domain+":"+port+path1+REMOVE_URL;
	}
	
	public String putUrl(){
		String path1=path==null?"":path;
		return domain+":"+port+path1+PUT_URL;
	}
	
}
