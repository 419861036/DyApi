package kkd.common.thread;

import java.util.Map;

import kkd.common.util.HttpUtil;

public class PostTask extends Task{
	
	private String url=null;
	Map<String, String> params=null;
	private CallBack cb=null;
	public PostTask(String url,Map<String, String> params) {
		this.url=url;
		this.params=params;
	}
	public PostTask(String url,Map<String, String> params,CallBack cb) {
		this.url=url;
		this.params=params;
		this.cb=cb;
	}
	
	@Override
	public void execute() {
		if(url!=null){
			try {
//				System.out.println(url);
				String rs=HttpUtil.post(url,params);
				super.success=true;
				if(cb!=null){
					cb.success(rs);
				}
			} catch (Exception e) {
				super.success=false;
//				e.printStackTrace();
			}
			
			
		}
		
	}
	public interface CallBack{
		void success(String rs);
	}
}
