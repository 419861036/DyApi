package kkd.common.cache;

public class CacheElement {
	
	private String key;
	private Object data;
	private Long timeout;
	private Long updateDate;
	
	
	
	public Long getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Long getTimeout() {
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	/**
	 * 判断是否过期
	 */
	public boolean isOutOfDate(){
		long ctm=System.currentTimeMillis();
		if(timeout!=null){
			if(ctm-updateDate>timeout){
				return true;
			}
		}
		return false;
	}
	/**
	 * 更新时间
	 */
	public void uppdate(){
		updateDate=System.currentTimeMillis();
	}
}
