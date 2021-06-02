package kkd.common.cache.client;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import kkd.common.exception.KKDValidationException;
import kkd.common.logger.LogWriter;
import kkd.common.thread.PutCacheTask;
import kkd.common.thread.RemoveCacheTask;
import kkd.common.thread.ServiceThread;
import kkd.common.thread.ThreadPool;
import kkd.common.util.HttpUtil;
import kkd.common.util.StringUtil;

public class CacheManager {

	private  List<ServerNode> unAvailableList = new ArrayList<ServerNode>();
	private  ConsistentHash<ServerNode> ch = null;
	private  Boolean init = false;
	
	//全局缓存设置
	private static Boolean cacheAble=true;
	//get缓存设置  仅对get方法控制
	private static Boolean cachePutAble=true;
	
	public CacheManager(String config) {
		if(!init){
			System.out.println("create cache Manager");
			if (StringUtil.isEmpty(config)) {
				throw new KKDValidationException("server 不能为空");
			}
			Collection<ServerNode> serverNodeList = parseConfig(config);
			HashFunction hf = DefaultHashAlgorithm.NATIVE_HASH;
			ch = new ConsistentHash<ServerNode>(hf, 10, serverNodeList);

			ClientThread ct = new ClientThread(ch);
			Thread t = new Thread(ct,"Cache-ClientThread");
			ct.setThread(t,unAvailableList);
			t.setDaemon(true);
			t.start();
			init=true;
		}
	}

	
	public static CacheManager getInstance() {
//		return getInstance(KKDConstants.getGlobalCommonCacheConfig());
		throw new RuntimeException("方法废弃 请调用 getInstance(String config)");
	}
	
	public Object get(String key) {
		if(!cacheAble||!cachePutAble){
			return null;
		}
		ServerNode sn = null;
		try {
			sn = ch.get(key.toString());
			if(sn==null){
				return null;
			}
			return sn.get(key);
		} catch (Exception e) {
			removeServer(sn, e);
		}
		return null;
	}
	
	public Object get(String key,Boolean upDate) {
		if(!cacheAble||!cachePutAble){
			return null;
		}
		ServerNode sn = null;
		try {
			sn = ch.get(key.toString());
			if(sn==null){
				return null;
			}
			return sn.get(key,upDate);
		} catch (Exception e) {
			removeServer(sn, e);
		}
		return null;
	}
	
	public String getInnerData(String key,String args,Boolean upDate) {
		if(!cacheAble||!cachePutAble){
			return null;
		}
		ServerNode sn = null;
		try {
			sn = ch.get(key.toString());
			if(sn==null){
				return null;
			}
			return sn.get(key,true,args,upDate);
		} catch (Exception e) {
			removeServer(sn, e);
		}
		return null;
	}

	public void removeServer(ServerNode sn, Exception e) {
		LogWriter.error("",e);
		if(e.getCause()!=null&&e.getCause() instanceof SocketTimeoutException){
		}else if(e.getCause()!=null&&e.getCause() instanceof  ConnectException){
		}else if(e.getCause()!=null&&e.getCause() instanceof  BindException){
		}
		else if(e .getCause() !=null && e.getCause() instanceof IOException){
			if (sn != null) {
				LogWriter.debug("remove server");
				ch.remove(sn);
				unAvailableList.add(sn);
			}
		}
	}
	
	public void remove(String key) {
		if(!cacheAble){
			return ;
		}
		RemoveCacheTask newTask=new RemoveCacheTask(ch,this,key);
		ThreadPool.getInstance().addTask(newTask);
	}
	
	public void  removeSync(String key) {
		if(!cacheAble){
			return ;
		}
		ServerNode sn = null;
		try {
			sn = ch.get(key.toString());
			if(sn==null){
				return ;
			}
			sn.remove(key);
			return ;
		} catch (Exception e) {
			this.removeServer(sn, e);
		}
	}

	public void  put(String key, long timeout, Object value) {
		if(!cacheAble){
			return ;
		}
		PutCacheTask newTask=new PutCacheTask(ch,unAvailableList,key,timeout,value);
		ThreadPool.getInstance().addTask(newTask);
	}
	
	public void  putSync(String key, long timeout, Object value) {
		if(!cacheAble){
			return ;
		}
		ServerNode sn = null;
		try {
			sn = ch.get(key.toString());
			if(sn==null){
				return ;
			}
			sn.put(key, timeout, value);
			return ;
		} catch (Exception e) {
			this.removeServer(sn, e);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String config = "domain=http://game.kkdian.com&port=80|domain=http://game.kkdian.com&port=80";
		CacheManager cm=new CacheManager(config);
		cm.get("a");
//		CacheManager  cm=CacheManager.getInstance(config);
	}

	private  List<ServerNode> parseConfig(String config) {
		String[] server = config.split("\\|");
		List<ServerNode> list = new ArrayList<ServerNode>();
		for (int i = 0; i < server.length; i++) {
			Map<String, String> param = StringUtil.getParaCollection(server[i], "&", "=");
			ServerNode sn = new ServerNode();
			sn.setDomain(param.get("domain"));
			sn.setPort(Integer.valueOf(param.get("port")));
			sn.setPath(param.get("path"));
			sn.setType(param.get("type"));
			list.add(sn);
		}
		return list;
	}
	
	
	public static Boolean getCachePutAble() {
		return cachePutAble;
	}

	public static void setCachePutAble(Boolean cachePutAble) {
		CacheManager.cachePutAble = cachePutAble;
	}

	public static Boolean getCacheAble() {
		return cacheAble;
	}

	public static void setCacheAble(Boolean cacheAble) {
		CacheManager.cacheAble = cacheAble;
	}


	public final static class ClientThread extends ServiceThread {
		private ConsistentHash<ServerNode> serverList = null;
		private volatile static Thread clientThread;
		private List<ServerNode> unAvailableList;
		public ClientThread(ConsistentHash<ServerNode> ch) {
			this.serverList = ch;
		}
		public void setThread(Thread clientThread,List<ServerNode> unAvailableList){
			ClientThread.clientThread=clientThread;
			this.unAvailableList=unAvailableList;
		}
		public void run() {
			synchronized (unAvailableList) {
				while (!isStoped()) {
					if (unAvailableList != null && serverList != null) {
						for (int i = 0; i < unAvailableList.size(); i++) {
							ServerNode  sn=unAvailableList.get(i);
							if(sn!=null){
								if (HttpUtil.isAvailable(sn.putUrl())) {
									serverList.add(sn);
									unAvailableList.remove(i);
									i=i-1;
								}
							}
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								LogWriter.error("",e);
							}
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						LogWriter.error("",e);
					}
				}
				try {
					if(ClientThread.clientThread!=null &&ClientThread.clientThread.isInterrupted()){
						ClientThread.clientThread.interrupt();
					}
				} catch (Exception e) {
				}
			}
		
			
			
		}
		@Override
		public String getServiceName() {
			return "Cache-ClientThread";
		}

	}
}
