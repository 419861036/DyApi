package kkd.common.cache.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kkd.common.cache.CacheElement;
import kkd.common.logger.LogWriter;
import kkd.common.thread.ServiceThread;
import kkd.common.util.StringUtil;

public class MapServerThread extends ServiceThread{
	
	private static int DAFAULT_TIMEOUT=1000*6;
	@SuppressWarnings("rawtypes")
	private Hashtable map=new Hashtable();// 用于Cache内容的存储
	private List<String> keyList=Collections.synchronizedList(new LinkedList<String>());
	private volatile static Thread thread;
	private MapServerThread(){
		super();
		this.start();
	}
	
	private static  MapServerThread st;
	
	public 	static MapServerThread getInstance(){
		if(st==null){
			st=new MapServerThread();
		}
		return st;
	}
	
	public void run() {
		while(!isStoped()){
			try {
				boolean isRemove=false;
				for (int i = 0; i < keyList.size(); i++) {
					isRemove=false;
					String key=keyList.get(i);
					if(key==null){
						keyList.remove(i);
						isRemove=true;
						continue;
					}
					CacheElement ce = (CacheElement) map.get(key);
					if(ce!=null){
						if(ce.isOutOfDate()){
							LogWriter.debug("clear key:{"+key+"} removed");
							map.remove(key);
							keyList.remove(i);
							isRemove=true;
						}
					}else{
						keyList.remove(i);
						isRemove=true;
					}
					if(isRemove){
						i=i-1;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				LogWriter.error("",e);
			}
		}
		try {
			if(MapServerThread.thread!=null &&MapServerThread.thread.isInterrupted()){
				MapServerThread.thread.interrupt();
			}
		} catch (Exception e) {
		}
	}
	
	public String get(String key, Boolean upDate){
		CacheElement ce = (CacheElement) map.get(key);
		String data="";
		if(ce !=null ){
//			System.out.println("key:{"+key+"} data:"+ce.getData());
			if(ce.isOutOfDate()){
				remove(key);
			}else{
				if(ce.getData()!=null){
					if(upDate!=null && upDate){
						ce.setUpdateDate(System.currentTimeMillis());
					}
					data=ce.getData().toString();
				}
			}
		}
		return data;
	}
	
	public String getInnerData(String key,String args, Boolean upDate){
		CacheElement ce = (CacheElement) map.get(key);
		String data="";
		if(ce !=null ){
//			System.out.println("key:{"+key+"} data:"+ce.getData());
			if(ce.isOutOfDate()){
				remove(key);
			}else{
				if(ce.getData()!=null){
					if(upDate!=null && upDate){
						ce.setUpdateDate(System.currentTimeMillis());
					}
					data=ce.getData().toString();
					ByteArrayInputStream byteArrayInputStream;
					try {
						byteArrayInputStream = new ByteArrayInputStream(data.getBytes("ISO-8859-1"));
						ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);  
						Map<String,Object> id = (Map<String,Object>)objectInputStream.readObject();
						if(id!=null){
							if(id.get(args)!=null){
								data=id.get(args).toString();
								if(!StringUtil.isEmpty(data)){
									return data;
								}
							}else{
								return "";
							}
						}
						return "";
					} catch (UnsupportedEncodingException e) {
						LogWriter.error("",e);
					} catch (IOException e) {
						LogWriter.error("",e);
					} catch (ClassNotFoundException e) {
						LogWriter.error("",e);
					}
				}
			}
		}
		return data;
	}
	
	
	public void put(String key,Long timeout,String value){
		timeout=timeout==null? DAFAULT_TIMEOUT:timeout;
		CacheElement ce=new CacheElement();
		ce.setKey(key);
		ce.setData(value);
		ce.setTimeout(timeout);
		ce.setUpdateDate(System.currentTimeMillis());
		cacheData(ce);
		keyList.add(key);
	}
	
	@SuppressWarnings("unchecked")
	public void cacheData(CacheElement em) {
		map.put(em.getKey(), em);
	}
	
	public void remove(String key){
		map.remove(key);
	}

	@Override
	public String getServiceName() {
		return "Cache-MapServerThread";
	}

	
}
