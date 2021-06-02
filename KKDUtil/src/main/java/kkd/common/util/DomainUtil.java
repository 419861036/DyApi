package kkd.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 域名操作类
 * @author Administrator
 *
 */
public class DomainUtil {
	/**
	 * 获取的根域名
	 * @param request
	 * @return
	 */
	public static String getRootDomain(String subDomain){
		Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(subDomain);
		if(matcher.find()){
			return matcher.group();
		}
		return null;
	}
	
	/**
	 * 获取域名
	 * @param subDomain
	 * @return
	 */
	public static String getDomain(String url){
		if(!StringUtil.isEmpty(url)){
			String pro="://";
			int pos=url.indexOf(pro);
			if(pos!=-1){
				pos=pos+pro.length();
				int pos1=url.indexOf('/', pos);
				pos1=pos1==-1?url.length():pos1;
				String domain=url.substring(pos,pos1);
				return domain;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String domain="http://www.blogjava.net/tinguo002/archive/2014/07/23/416121.html";
		String s=getDomain(domain);
		System.out.println(s);
	}
}
