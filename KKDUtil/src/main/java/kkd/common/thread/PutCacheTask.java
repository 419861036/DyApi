package kkd.common.thread;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collection;

import kkd.common.cache.client.ConsistentHash;
import kkd.common.cache.client.ServerNode;
import kkd.common.exception.KKDException;
import kkd.common.logger.LogWriter;


public class PutCacheTask extends Task{
	
	private ConsistentHash<ServerNode> ch;
	private Collection<ServerNode> unAvailableList;
	private String key;
	private long timeout;
	private Object value;
	public PutCacheTask(ConsistentHash<ServerNode> ch, Collection<ServerNode> unAvailableList, String key, long timeout, Object value) {
		this.ch=ch;
		this.unAvailableList=unAvailableList;
		this.key=key;
		this.timeout=timeout;
		this.value=value;
	}
	@Override
	public void execute() {
		ServerNode sn=null;
		try {
			sn = ch.get(key);
			if(sn!=null){
				sn.put(key, timeout, value);
			}
		} catch (KKDException e) {
			LogWriter.debug("", e);
			if(e.getCause()!=null&&e.getCause() instanceof SocketTimeoutException){
				
			}else if(e .getCause() !=null && e.getCause() instanceof IOException){
				if (sn != null) {
					LogWriter.debug("remove server");
					ch.remove(sn);
					unAvailableList.add(sn);
				}
			}
		}
	}
}
