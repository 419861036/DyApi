package kkd.common.thread;

import kkd.common.cache.client.CacheManager;
import kkd.common.cache.client.ConsistentHash;
import kkd.common.cache.client.ServerNode;


public class RemoveCacheTask extends Task{
	
//	private ServerNode sn;
	private ConsistentHash<ServerNode> ch;
	private CacheManager cm;
	private String key;
	public RemoveCacheTask(ConsistentHash<ServerNode> ch,CacheManager cm , String key) {
		this.ch=ch;
		this.cm=cm;
		this.key=key;
	}
	@Override
	public void execute() {
		ServerNode sn = null;
		try {
			sn = ch.get(key.toString());
			if(sn!=null){
				sn.remove(key);
			}
		} catch (Exception e) {
			cm.removeServer(sn, e);
		}
	}
}
