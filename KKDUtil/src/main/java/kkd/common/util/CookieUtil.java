package kkd.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie 操作类
 * 
 * @author tanbin
 * 
 */
public class CookieUtil {
	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, Integer maxAge) {
		addCookie(response, name, value, "/", null, maxAge);
	}

	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param domain
	 *            域名
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String domain,
			String name, String value, Integer maxAge) {
		addCookie(response, name, value, "/", domain, maxAge);
	}

	public static void addCookie(HttpServletResponse response, String name,
			String value, String uri, String domain, Integer maxAge) {
		Cookie cookie = new Cookie(name, value);
		if (uri != null)
			cookie.setPath(uri);
		if (maxAge != null)
			cookie.setMaxAge(maxAge);
		if (domain != null)
			cookie.setDomain(domain);
		response.addCookie(cookie);
	}

	/**
	 * setMaxAge方法设置cookie经过多长秒后被删除。如果参数是0，就说明立即删除。如果是负数就表明当浏览器关闭时自动删除。
	 * 
	 * @param response
	 * @param name
	 */
	public static void remove(HttpServletResponse response,String path,String domain, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		if(path!=null)
		cookie.setPath(path);
		if(domain!=null)
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}


	/**
	 * 
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * 
	 * @param name
	 *            cookie名字
	 * 
	 * @return
	 */

	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		if (name == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * 
	 * @return
	 */

	public static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}
