package kkd.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 参数处理
 * @author 
 *
 */
public class ParamUtil {
	/**
	 * 移除参数
	 * @param url
	 * @param removeKey
	 * @return
	 */
	public static String removeParam(String url,String[] removeKeys){
		if(url==null){
			return  null;
		}
		int pos=url.indexOf("?");
		if(pos!=-1){
			String baseUrl=url.substring(0, pos);
			String queryString=url.substring(pos+1,url.length());
			Map<String,String> para=StringUtil.getParaCollection(queryString, "&", "=");
			int len=para.size();
			if(removeKeys!=null){
				for(int i=0;i<removeKeys.length;i++){
					para.remove(removeKeys[i]);
				}
			}
			
			StringBuffer sb=new StringBuffer();
			if(para.size()>0){
				sb.append("?");
			}
			Set<Map.Entry<String,String>> key = para.entrySet();
	        for (Iterator<Map.Entry<String, String>> it = key.iterator(); it.hasNext();) {
	        	Map.Entry<String,String> entry = it.next();
	        	sb.append(entry.getKey());
	        	sb.append("=");
	        	sb.append(entry.getValue());
	        	if(it.hasNext()){
	        		sb.append("&");
	        	}
	        }
	        if(para.size()!=len){
	        	url=baseUrl+sb;
	        }
		}
		return url;
	}
	
}
