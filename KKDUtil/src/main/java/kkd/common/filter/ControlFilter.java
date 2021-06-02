package kkd.common.filter;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.entity.Msg;
import kkd.common.exception.KKDException;
import kkd.common.exception.KKDNetworkExcepttion;
import kkd.common.invoke.InvokeUtil;
import kkd.common.invoke.MethodAccess;
import kkd.common.logger.LogWriter;
import kkd.common.util.StringUtil;
public class ControlFilter implements Filter {
	
	private static final ResourceBundle config = ResourceBundle
	.getBundle("mapping");
	private static final String SUFFIX_1 = ".do";//内部
	private static final String SUFFIX_2 = ".jsp";//需要模拟jsp后缀的
	private static final String SUFFIX_3 = ".action";//不做权限处理请求
	private static final String SUFFIX_5 = "/";//模拟servlet
	private static final String SUFFIX_4 = ".jif";//第三方接口
	private static final String HTML = "html";
	private static final String XML = "xml";
	private static final String JSON = "json";
	
    public void destroy() {
    	
    }
      
    
    
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req=(HttpServletRequest)request;
        try {
        	String path=req.getServletPath();
        	int s=path.indexOf('/', 1);
        	path=path.substring(s);
			String value;
			String className;
			String methodName;
			if (path != null ) {
				if(path.endsWith(SUFFIX_1)
						|| path.endsWith(SUFFIX_2)
						|| path.endsWith(SUFFIX_3)
						|| path.endsWith(SUFFIX_4)
						|| path.endsWith(SUFFIX_5)
						){
					value = config.getString(path);
					Map<String, String> map = StringUtil.getParaCollection(value, ";", ":");
					className = map.get("className");
					methodName = map.get("methodName");
					MethodAccess ma = InvokeUtil.getMethodAccess(Class
							.forName(className));
					Object o = ma.newInstance();
					ma.invoke(o, methodName,req, resp);
				}
			}
		} catch (KKDNetworkExcepttion e) {
			showException(req, resp, e.getCode() , "网络异常请稍后重试！");
		} catch (KKDException e) {
			showException(req, resp, e.getCode(), e.getMessage());
		} catch (Exception e) {
			LogWriter.error("", e);
			showException(req, resp, Msg.ERROR, "系统错误");
		}
    }

    public void init(FilterConfig arg0) throws ServletException {
    	
    }
    
    
    private void showException(HttpServletRequest req,
			HttpServletResponse resp, int code, String msg)
			throws IOException {
		String format;
		format = (String)req.getAttribute("sys_format");
		format = StringUtil.isEmpty(format) ? HTML : format;
		if (XML.equals(format)) {
			printMsg(resp, code, msg);
		}else if (JSON.equals(format)) {
			printJsonMsg(resp, code, msg);
		}else {
			resp.getWriter().println(msg);
		}
	}

	public void printMsg(HttpServletResponse resp, int code, String msgStr) {
		Msg<String> msg = new Msg<String>(code);
		msg.setMsg(msgStr);
		try {
			resp.getWriter().print(msg.toXMls(Msg.class));
		} catch (IOException e1) {
		}
	}
	public void printJsonMsg(HttpServletResponse resp, int code, String msgStr) {
		Msg<String> msg = new Msg<String>(code);
		msg.setMsg(msgStr);
		try {
			resp.getWriter().print(msg.objectToFastJSON());
		} catch (IOException e1) {
		}
	}

	public void printError(HttpServletResponse resp, Exception e) {
		e.printStackTrace();
		printMsg(resp, Msg.ERROR, "系统错误");
	}

}
