package kkd.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.dao.Paginator;
import kkd.common.entity.Msg;
import kkd.common.exception.KKDValidationException;
import kkd.common.util.HttpInvoke;
import kkd.common.util.StringUtil;
import kkd.common.util.WebUtil;

/**
 * Servlet基类
 * 
 * @author Administrator
 * 
 */
public class BaseServlet {
	/**
	 * 获取分页对象
	 * @param pageIndex 当前页码
	 * @param pageSize 每页显示数量
	 * @param req
	 * @return 分页对象
	 */
	public Paginator getPaginator(HttpServletRequest req) {
		Integer pageIndex = getInteger(req, "page");
		Integer pageSize = getInteger(req, "itemsPerPage");
		pageIndex = pageIndex == null ? 1 : pageIndex;
		pageSize = pageSize == null ? 20 : pageSize;
		return new Paginator(pageIndex, pageSize);
	}
	public String getString(HttpServletRequest req, String key) {
		return WebUtil.getString(req, key);
	}
	
	public String getString(HttpServletRequest req, String key,String tip) {
		String t=WebUtil.getString(req, key);
		if(StringUtil.isEmpty(t)){
			throw new KKDValidationException(tip);
		}
		return t;
	}
	
	public Integer getInteger(HttpServletRequest req, String key) {
		return WebUtil.getInteger(req, key);
	}
	
	public Integer getInteger(HttpServletRequest req, String key,String tip) {
		Integer t=WebUtil.getInteger(req, key);
		if(t==null){
			throw new KKDValidationException(tip);
		}
		return t;
	}
	
	public Long getLong(HttpServletRequest req, String key) {
		return WebUtil.getLong(req, key);
	}
	
	public Long getLong(HttpServletRequest req, String key,String tip) {
		Long l= WebUtil.getLong(req, key);
		if(l==null){
			throw new KKDValidationException(tip);
		}
		return l;
	}
	
	public Double getDouble(HttpServletRequest req, String key) {
		Double d= WebUtil.getDouble(req, key);
		return d;
	}
	
	public Double getDouble(HttpServletRequest req, String key,String tip) {
		Double d= WebUtil.getDouble(req, key);
		if(d==null){
			throw new KKDValidationException(tip);
		}
		return d;
	}
	
	public Float getFloat(HttpServletRequest req, String key) {
		return WebUtil.getFloat(req, key);
	}
	
	public Float getFloat(HttpServletRequest req, String key,String tip) {
		Float d= WebUtil.getFloat(req, key);
		if(d==null){
			throw new KKDValidationException(tip);
		}
		return d;
	}
	
	public Boolean getBoolean(HttpServletRequest req, String key) {
		return WebUtil.getBoolean(req, key);
	}
	
	public Boolean getBoolean(HttpServletRequest req, String key,String tip) {
		Boolean d= WebUtil.getBoolean(req, key);
		if(d==null){
			throw new KKDValidationException(tip);
		}
		return d;
	}
	
	
	
	public Date getDate(HttpServletRequest req, String key) {
		return WebUtil.getDate(req, key);
	}
	
	public Date getDate(HttpServletRequest req, String key,String pattern) {
		return WebUtil.getDate(req, key,pattern);
	}
	
	public Date getDate(HttpServletRequest req, String key,String pattern,String tip) {
		try {
			Date d= WebUtil.getDate(req, key,pattern);
			if(d==null){
				throw new KKDValidationException(tip);
			}
			return d;
		} catch (Exception e) {
			throw new KKDValidationException(tip);
		}
	}
	/**
	 * 便于框架判断
	 * 输出json格式
	 * @param req
	 */
	public static void jsonFormat(HttpServletRequest req){
		req.setAttribute("sys_format", "json");
	}
	/**
	 * 便于框架判断
	 * 输出xml格式
	 * @param req
	 */
	public static void xmlFormat(HttpServletRequest req){
		req.setAttribute("sys_format", "xml");
	}
	/**
	 * 便于框架判断
	 * 输出html格式
	 * @param req
	 */
	public static void htmlFormat(HttpServletRequest req){
		req.setAttribute("sys_format", "html");
	}
	
	/**
	 * 便于框架判断
	 * 输出html格式
	 * @param req
	 */
	public static void objFormat(HttpServletRequest req){
		req.setAttribute("sys_format", "obj");
	}
	
	@SuppressWarnings("deprecation")
	public void printMsg( HttpServletResponse resp,int code,String msgStr){
		Msg<String> msg = new Msg<String>(code);
		msg.setMsg(msgStr);
		try {
			resp.getWriter().print(msg.toJSON(null));
		} catch (IOException e1) {
		}
	}
	public void printError( HttpServletResponse resp,Exception e){
		e.printStackTrace();
		printMsg(resp,Msg.ERROR, "系统错误");
	}
	public String printMsgStr(int code,String msg){
		StringBuilder sb=new StringBuilder("<Msg>");
		sb.append("<code>");
		sb.append(code);
		sb.append("</code>");
		sb.append("<msg>");
		sb.append(msg);
		sb.append("<msg>");
		sb.append("</Msg>");
		return sb.toString();
	}
	
	public static void  setFormat(HttpServletRequest request, String format) {
		if(!StringUtil.isEmpty(format) ) {
			if("xml".equals(format)){
				xmlFormat(request);
			}else if("json".equals(format)){
				jsonFormat(request);	
			}else if("obj".equals(format)){
				objFormat(request);
			}else{
				htmlFormat(request);
			}
				
		}
		else
			htmlFormat(request);
	}
	
	public  void setFormat(HttpServletRequest request){
		String format = this.getString(request, "format");
		setFormat(request,format);
	}
	@SuppressWarnings("rawtypes")
	public void resPonseWriter(HttpServletRequest req,HttpServletResponse resp, String format,String url, Msg msg, Class ... cls) 
				throws IOException, ServletException{
		PrintWriter w = resp.getWriter();
		if(!StringUtil.isEmpty(format)){
			if("xml".equals(format))
				w.print(msg.toXMls(cls));
			else if("json".equals(format))
				w.print(msg.objectToFastJSON());
			else
				req.getRequestDispatcher(url).forward(req, resp);	
		}else{
			req.getRequestDispatcher(url).forward(req, resp);
		}
		
	}
	
	public void resPonseWriter(HttpServletRequest req,HttpServletResponse resp,String url, Msg<?> msg, Class<?> ... cls) 
			throws IOException, ServletException{
		this.resPonseWriter(req, resp, this.getString(req, "format"), url, msg, cls);
	}
	
	
	@SuppressWarnings("rawtypes")
	public void returnFormat(HttpServletRequest request, HttpServletResponse response , HttpInvoke invoke,  Msg<?> msg) throws IOException {
		PrintWriter out = response.getWriter();
		String jifFormat = getString(request, "format");
		if("json".equals(jifFormat)) {
			out.print(msg.objectToFastJSON());
		}else {
			try {
				out.print(invoke.serializable(msg));
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.print(invoke.serializable(new Msg()));
		}
	}
	
}
