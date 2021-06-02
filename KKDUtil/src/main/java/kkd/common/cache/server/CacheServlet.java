package kkd.common.cache.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.cache.server.ServerThread;
import kkd.common.servlet.BaseServlet;
import kkd.common.util.StringUtil;

/**
 * 缓存服务端
 * @author tanbin
 *
 */
public class CacheServlet extends BaseServlet{
	
	/**
	 * 
	 * @param key
	 * @param response
	 * @throws IOException
	 * @return data
	 */
	public void get(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String key=getString(req, "key");
		String type=getString(req, "type");
		String innerArg=getString(req, "innerArg");
		Boolean inner=getBoolean(req, "inner");
		Boolean upDate=getBoolean(req, "upDate");
		String value="";
		if(type==null ||StringUtil.equals("Cache", type)){
			ServerThread st =ServerThread.getInstance();
			if(!StringUtil.isEmpty(key)){
				if(inner!=null&&inner){
					value=st.getInnerData(key,innerArg, upDate);
				}else{
					value=st.get(key,upDate);
				}
			}
		}else if(StringUtil.equals("Mem", type)){
			MapServerThread st =MapServerThread.getInstance();
			if(!StringUtil.isEmpty(key)){
				if(inner!=null&&inner){
					value=st.getInnerData(key,innerArg, upDate);
				}else{
					value=st.get(key,upDate);
				}
			}
		}
		response.getWriter().print(value);
	}
	
	
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param key
	 * @param timeout
	 * @return void
	 */
	public void put(HttpServletRequest req, HttpServletResponse response){
		String key=getString(req, "key");
		String type=getString(req, "type");
		String value=getString(req, "value");
		Long timeout=getLong(req, "timeout");
		if(type==null ||StringUtil.equals("Cache", type)){
			if(StringUtil.isEmpty(key)||value==null){
				return;
			}
			ServerThread st =ServerThread.getInstance();
			st.put(key, timeout, value);
		}else if(StringUtil.equals("Mem", type)){
			if(StringUtil.isEmpty(key)||value==null){
				return;
			}
			MapServerThread st =MapServerThread.getInstance();
			st.put(key, timeout, value);
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @return void
	 */
	public void remove(HttpServletRequest req, HttpServletResponse response){
		String key=getString(req, "key");
		String type=getString(req, "type");
		if(type==null ||StringUtil.equals("Cache", type)){
			if(StringUtil.isEmpty(key)){
				return;
			}
			ServerThread st =ServerThread.getInstance();
			st.remove(key);
		}else if(StringUtil.equals("Mem", type)){
			if(StringUtil.isEmpty(key)){
				return;
			}
			MapServerThread st =MapServerThread.getInstance();
			st.remove(key);
		}
	
	}
	
}
