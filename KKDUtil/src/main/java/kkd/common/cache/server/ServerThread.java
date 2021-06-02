package kkd.common.cache.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kkd.common.cache.CacheElement;
import kkd.common.logger.LogWriter;
import kkd.common.thread.ServiceThread;
import kkd.common.util.StringUtil;

public class ServerThread extends ServiceThread{
	
	private static int DAFAULT_TIMEOUT=1000*6;
	@SuppressWarnings("rawtypes")
	private Hashtable map=new Hashtable();// 用于Cache内容的存储
	@SuppressWarnings("rawtypes")
	private ReferenceQueue q=new ReferenceQueue();// 垃圾Reference的队列
	private List<String> keyList=Collections.synchronizedList(new LinkedList<String>());
	private volatile static Thread thread;
	
	private ServerThread(){
		super();
		this.start();
	}
	
	// 继承SoftReference，使得每一个实例都具有可识别的标识。
	// 并且该标识与其在HashMap内的key相同。
	@SuppressWarnings("rawtypes")
	private class CacheRef extends SoftReference {
		private String _key = "";

		@SuppressWarnings("unchecked")
		public CacheRef(CacheElement em, ReferenceQueue q) {
			super(em, q);
			_key = em.getKey();
		}
	}
	
	private static final ServerThread st=new ServerThread();
	public 	static ServerThread getInstance(){
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
					CacheElement ce= null;
					// 缓存中是否有该Employee实例的软引用，如果有，从软引用中取得。
					CacheRef ref = (CacheRef) map.get(key);
					if(ref!= null){
						ce = (CacheElement) ref.get();
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
				cleanCache();
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
			if(ServerThread.thread!=null &&ServerThread.thread.isInterrupted()){
				ServerThread.thread.interrupt();
			}
		} catch (Exception e) {
		}
	}
	
	public String get(String key, Boolean upDate){
		CacheElement ce= null;
		// 缓存中是否有该Employee实例的软引用，如果有，从软引用中取得。
		CacheRef ref = (CacheRef) map.get(key);
		if(ref!= null){
			ce = (CacheElement) ref.get();
		}
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
	
	/**
	 * 获取内部对象数据
	 * @param innerArg 
	 * @return
	 */
	public String getInnerData(String key, String args, Boolean upDate){
		CacheElement ce= null;
		// 缓存中是否有该Employee实例的软引用，如果有，从软引用中取得。
		CacheRef ref = (CacheRef) map.get(key);
		if(ref!= null){
			ce = (CacheElement) ref.get();
		}
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
	
	
	// 以软引用的方式对一个Employee对象的实例进行引用并保存该引用
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
		cleanCache();// 清除垃圾引用
		CacheRef ref = new CacheRef(em, q);
		map.put(em.getKey(), ref);
	}
	public void remove(String key){
		map.remove(key);
		cleanCache();
	}
	private void cleanCache() {
		CacheRef ref = null;
		while ((ref = (CacheRef) q.poll()) != null) {
			map.remove(ref._key);
		}
	}

	@Override
	public String getServiceName() {
		return "Cache-ServerThread";
	}
	
}
