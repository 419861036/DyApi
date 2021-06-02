package com.exe.web;

import com.exe.web.util.DbConfigUtil;
import com.exe.web.util.MyConfUtil;
import kkd.common.dao.dbuitl.BaseDataSourceProviderImpl;
import kkd.common.dao.dbuitl.JDBC;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.io.File;


public class AppServerMain {
	static{
		//System.setProperty("logback.configurationFile", "F:\\code_store\\new_yqyx\\module\\exe\\exeWeb\\target\\conf\\logback.xml");
		String conf=MyConfUtil.getRootPath()+File.separator+"conf"+File.separator;
		String logxml=conf+"logback.xml";
		System.setProperty("logback.configurationFile", logxml);
		System.out.println("logxml:"+logxml);
	}

	// private final static Logger logger = LoggerFactory.getLogger(AppServerMain.class);
	public static void regPlugins(){
		// Properties conf=PluginConfUtil.getConf();
		// for (Map.Entry<Object,Object> ele : conf.entrySet()) {
		// 	String key=ele.getKey().toString();
		// 	String val=ele.getValue().toString();
		// 	Class cls;
		// 	try {
		// 		cls = Class.forName(val);
		// 		CmdHandle.register(key, cls);
		// 	} catch (ClassNotFoundException e) {
		// 		// logger.error("", e);
		// 		e.printStackTrace();
		// 	}
		// }
		
	}
	
	public static void initDb(String code){
		BaseDataSourceProviderImpl bdspi=BaseDataSourceProviderImpl.getInstance();
		bdspi.setDbConfig(code, DbConfigUtil.getConfig());
		JDBC.setDefultDataSourceProvider(bdspi,code);
	}
	
	
//	public static List<RuleVo> initData(Integer ruleId){
//		Msg<List<RuleVo>> msg=new Msg<>();
//		RuleBo.list(msg);
//		return msg.getV();
//	}
	
	public static void initServer(Integer port){
		// 创建服务器
		Server server = new Server();
		if(true){
			// 设置ssl连接器
//			SslSocketConnector ssl_connector = new SslSocketConnector();
//			ssl_connector.setPort(port);
//			SslContextFactory cf = ssl_connector.getSslContextFactory();
//			cf.setKeyStorePath("src/test/java/jetty/epayService.keystore");
//			cf.setKeyStorePassword("123456");
//			cf.setKeyManagerPassword("123456");
//			server.addConnector(ssl_connector);
			HttpConfiguration https_config = new HttpConfiguration();
			https_config.setSecureScheme("https");
			https_config.setSecurePort(port);
			https_config.setOutputBufferSize(32768);
			https_config.addCustomizer(new SecureRequestCustomizer());

			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStoreType("PKCS12");
			String path=MyConfUtil.getRootPath()+"/conf"+"/zs/keystore.p12";
			sslContextFactory.setTrustStorePath(path);
			sslContextFactory.setKeyStorePath(path);
			sslContextFactory.setKeyStorePassword("123456");
			sslContextFactory.setKeyManagerPassword("123456");

			ServerConnector httpsConnector = new ServerConnector(server,
					new SslConnectionFactory(sslContextFactory,"http/1.1"),
					new HttpConnectionFactory(https_config));
			httpsConnector.setPort(port);
			httpsConnector.setIdleTimeout(500000);
			server.addConnector(httpsConnector);
		}

		try {
			HandlerList list = new HandlerList();
			ContextHandler contextHandler = new ContextHandler("/");
			
			ResourceHandler handler = new ResourceHandler();  //静态资源处理的handler
			handler.setDirectoriesListed(true);  //会显示一个列表
			handler.setWelcomeFiles(new String[]{"index.html"});
			String resource=MyConfUtil.getStaticPath();
			handler.setResourceBase(resource);
			//logger.info("web base:{}",JSON.toJSONString(handler.getBaseResource()));
			contextHandler.setHandler(handler);
			list.addHandler(contextHandler);
//			server.setHandler(handler);
			
			ContextHandler contextHandler1 = new ContextHandler("/api");
			BaseHandle handle=new BaseHandle();
			contextHandler1.setHandler(handle);
			// 设置handler
//			server.setHandler(handle);
			list.addHandler(contextHandler1);
			
			server.setHandler(list);

//			 ServletHandler handler1 = new ServletHandler();
//			 AsyncProxyServlet servlet = new MyProxyServlet();
//			 ServletHolder proxyServletHolder = new ServletHolder(servlet);
//			 proxyServletHolder.setAsyncSupported(true);
//			 proxyServletHolder.setInitParameter("maxThreads", "100");
//			 handler.addServletWithMapping(proxyServletHolder, "/*");

			// 启动服务器
			server.start();
			// 阻塞Jetty server的线程池，直到线程池停止
			server.join();
		} catch (Exception e) {
			//logger.error("", e);
			e.printStackTrace();
		}
	}

	
	
	public static void main(String[] args) {
		//System 日志重定向到logback
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
		String p = System.getProperty("p");
		initDb("xml");
		regPlugins();
		Init.regPlugins();
		if(p==null){
			initServer(8080);
		}else{
			initServer(Integer.valueOf(p));
		}

	}
}
