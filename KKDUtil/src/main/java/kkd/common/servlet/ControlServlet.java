package kkd.common.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.entity.Msg;
import kkd.common.exception.KKDException;
import kkd.common.exception.KKDNetworkExcepttion;
import kkd.common.invoke.InvokeUtil;
import kkd.common.invoke.MethodAccess;
import kkd.common.logger.LogWriter;
import kkd.common.util.StringUtil;

/**
 * servlet中央控制器
 * 
 * 需要实现ServletHandle接口 请求必须以.do结尾
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class ControlServlet extends HttpServlet {

	private static  Properties config;
	private static final String SUFFIX_1 = ".do";//内部
	private static final String SUFFIX_2 = ".jsp";//需要模拟jsp后缀的
	private static final String SUFFIX_3 = ".action";//不做权限处理请求
	private static final String SUFFIX_5 = "/";//模拟servlet
	private static final String SUFFIX_4 = ".jif";//第三方接口
	private static final String HTML = "html";
	private static final String XML = "xml";
	private static final String JSON = "json";
	private static final String OBJ = "obj";

	public  void init(){
		if(config==null){
			config=new Properties();
			String path=this.getClass().getClassLoader().getResource("").getPath();
			File f=new File(path);
			File[] flist=f.listFiles();
			for (File file : flist) {
				if(file.getName().startsWith("mapping")&&file.getName().endsWith(".properties")){
					System.out.println("read mapping:"+file.getName());
					Properties p=getProperties(file);
					config.putAll(p);
				}
			}
		}
	}
	
	public static Properties getProperties(File location){
		Properties pro = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(location);
			pro.load(in);
			in.close();
			return pro;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		init();
		this.doPost(req, resp);
	}

	/**
	 * @param format返回数据格式
	 *            html/xml 默认html
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		init();
//		String format = null;
		try {
			String path = req.getPathInfo();
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
					value = config.getProperty(path);
					if(value==null){
						throw new RuntimeException("path is not found："+path);
					}
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
			e.printStackTrace();
			LogWriter.error("", e);
			showException(req, resp, Msg.ERROR, "系统错误");
		}
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
		}else if (OBJ.equals(format)) {
			printObjMsg(resp, code, msg);
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
	
	public void printObjMsg(HttpServletResponse resp, int code, String msgStr) {
		Msg<String> msg = new Msg<String>(code);
		msg.setMsg(msgStr);
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(msg);//写对象，序列化  
			out.flush();
			out.close();
			String serStr = URLEncoder.encode(byteOut.toString("ISO-8859-1"), "UTF-8");
			resp.getWriter().print(serStr);
		} catch (IOException e1) {
		}
	}
	public void printError(HttpServletResponse resp, Exception e) {
		e.printStackTrace();
		printMsg(resp, Msg.ERROR, "系统错误");
	}
}
