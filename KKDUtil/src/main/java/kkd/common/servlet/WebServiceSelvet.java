package kkd.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.invoke.InvokeUtil;
import kkd.common.invoke.MethodAccess;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * webservice 调用协议
 * @author tanbin
 *
 */
public class WebServiceSelvet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	/**
	 * webservice
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
//		String className=req.getParameter("className");
//		String methodName=req.getParameter("methodName");
		JSONObject jo=new JSONObject();
		String className = jo.getString("className");
		String methodName= jo.getString("methodName");
		try {
			Class<?> cls=Class.forName(className);
			MethodAccess ma = InvokeUtil.getMethodAccess(cls);
			JSONArray ja=jo.getJSONArray("p");
			int len=ja.size();
			Object[] obj=new Object[len];
			for (int i = 0; i <len ; i++) { 
				obj[i]=ja.getString(i);
			}
			ma.invoke(cls, methodName, obj);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
