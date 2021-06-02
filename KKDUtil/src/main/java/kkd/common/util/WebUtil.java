package kkd.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebUtil {
	/**
	 * 获取session 属性
	 * @param request
	 * @param name
	 * @return
	 */
	public static Object getSessionAttribute(HttpServletRequest request,
			String name) {
		HttpSession session = request.getSession(false);
		return (session != null ? session.getAttribute(name) : null);
	}
	/**
	 * 设置/移除session属性
	 * @param request
	 * @param name
	 * @param value value 为空移除属性
	 */
	public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {  
	    if (value != null) {  
	        request.getSession().setAttribute(name, value);  
	    }  
	    else {  
	        HttpSession session = request.getSession(false);  
	        if (session != null) {  
	            session.removeAttribute(name);  
	        }  
	    }  
	} 
	
	/**
	 * 将id=1,2,3变成 list
	 * @param ids
	 * @return
	 */
	public static List<Integer> getIds(HttpServletRequest req,String key){
		String ids=req.getParameter(key);
		List<Integer> list =new ArrayList<Integer>();
		if(!StringUtil.isEmpty(ids)){
			String[] idArr=ids.split(",");
			for (int i = 0; i < idArr.length; i++) {
				Integer id=Integer.parseInt(idArr[i]);
				list.add(id);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getIds(HttpServletRequest req,String key, Class<T> t){
		String ids=req.getParameter(key);
		List<T> list =new ArrayList<T>();
		if(!StringUtil.isEmpty(ids)){
			String[] idArr=ids.split(",");
			for (int i = 0; i < idArr.length; i++) {
				if(t.getName().equals("java.lang.Integer") ){
					T id=(T)Integer.valueOf(idArr[i]);
					list.add(id);
				}else if(t.getName().equals("java.lang.Long")){
					T id=(T)Long.valueOf(idArr[i]);
					list.add(id);
				}else{
					T id=(T)idArr[i];
					list.add(id);
				}
				
			}
		}
		return list;
	}
//	public static <T> T getClassT(Class<T> clazz){
//			Class<?> calzz=null;
//	        while(clazz != Object.class){
//	            Type t = clazz.getGenericSuperclass();
//	            if (t instanceof ParameterizedType) {
//	                ParameterizedType parameterizedType = (ParameterizedType) t;
//	                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//	                Type type = actualTypeArguments[0];
//	                if (type instanceof Class) {
//	                   calzz = (Class<T>) type;
//	                   break;
//	                }
//	            }
//	            clazz=(Class<T>) clazz.getSuperclass();
//	        }
//	        return (T) clazz;
//	}
	
	public static String getString(HttpServletRequest req, String key) {
		return req.getParameter(key);
	}
	public static Integer getInteger(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		return getInteger(value);
	}
	public static Integer getInteger(String value) {
		if (!StringUtil.isEmpty(value)) {
			if (StringUtil.isInteger(value)) {
				return Integer.valueOf(value);
			}
		}
		return null;
	}
	public static Long getLong(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		return getLong(value);
	}
	
	
	public static Long getLong(String value) {
		if (!StringUtil.isEmpty(value)) {
			return Long.valueOf(value);
		}
		return null;
	}
	public static Double getDouble(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		return getDouble(value);
	}
	public static Double getDouble(String value) {
		if (!StringUtil.isEmpty(value)) {
			if (StringUtil.isNumber(value)) {
				return Double.valueOf(value);
			}
		}
		return null;
	}
	public static Float getFloat(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		return getFloat(value);
	}
	public static Float getFloat(String value) {
		if (!StringUtil.isEmpty(value)) {
			if (StringUtil.isNumber(value)) {
				return Float.valueOf(value);
			}
		}
		return null;
	}
	public static Boolean getBoolean(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		return getBoolean(value);
	}
	public static Boolean getBoolean(String value) {
		if (!StringUtil.isEmpty(value)) {
			return Boolean.valueOf(value);
		}
		return null;
	}
	public static Date getDate(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		return getDate(value);
	}
	public static Date getDate(String value) {
		if (!StringUtil.isEmpty(value)) {
			return DateUtil.parse(value);
		}
		return null;
	}
	public static Date getDate(HttpServletRequest req,String key,String pattern) {
		String value = req.getParameter(key);
		return getDate(value,pattern);
	}
	public static Date getDate(String value,String pattern) {
		if (!StringUtil.isEmpty(value)) {
			return DateUtil.parse(value,pattern);
		}
		return null;
	}
	
	
	
	
	public static String getBasePath(HttpServletRequest req){
		String url = req.getScheme() + "://" + req.getServerName() + ":"
		+ req.getServerPort() + req.getContextPath();
		return url;
	}
	
	public static Map<String,String> getParameterMap(HttpServletRequest request){
		Map<?, ?> m = request.getParameterMap();
		Map<String,String> map=new HashMap<String, String>();
		Iterator<?> it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = ((String[]) m.get(k))[0];
			map.put(k, v);
		}
		return map;
	}
	
	public static SortedMap<String,String> getParameterSortMap(HttpServletRequest request){
		Map<?, ?> m = request.getParameterMap();
		SortedMap<String,String> map=new TreeMap<String, String>();
		Iterator<?> it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = ((String[]) m.get(k))[0];
			map.put(k, v);
		}
		return map;
	} 
	
	public static String getParameterSortMapToStr(HttpServletRequest request){
		Map<String,String> map = getParameterSortMap(request);
		return getSortMapToStr(map);
	}
	
	
	
	
	public static String getSortMapToStr(Map<String,String> map){
		Iterator<?> it = map.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = map.get(k);
			sb.append(k+"="+v+"&");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	
	/**
	 * 获取请求类型
	 * 
	 */
	public static String getRequestType(HttpServletRequest req){
		String XMLHttpRequest = req.getHeader("X-Requested-With");
		String accept = req.getHeader("Accept");
		String type="html";
		if ("XMLHttpRequest".equals(XMLHttpRequest)
				|| accept.indexOf("text/html") == -1) {
			if (!StringUtil.isEmpty(accept)) {
				if (accept.indexOf("application/json") != -1) {
					type="json";
				} else if (accept.indexOf("application/xml") != -1) {
					type="json";
				}
			}
		} 
		return type;
	}
	
	
	
	
	
}
