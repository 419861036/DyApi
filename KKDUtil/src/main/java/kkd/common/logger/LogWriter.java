package kkd.common.logger;

import kkd.common.util.EnvUtil;

/**
 * 
 * @author wdkj
 *
 */
public class LogWriter {
	private static ILogger logger = null;

	public static void setDefaultLogger() {
		logger = new DefaultLogger();
	}

	public synchronized static void setLogger(ILogger log) {
		logger = log;
	}
	
	/**
	 * 设置日志等级
	 * @param level
	 */
	public static void setDefaultLevel(String level){
		logger.setLevel(level);
	}


	public static void info(String msg) {
		if (logger != null) {
			logger.info(msg);
		}else{
			stdout(msg,null);
		}
	}
	private static void stdout(String msg, Throwable error){
		String tmp=String.format("%s:%s", EnvUtil.get(EnvUtil.projectName),msg);
		System.out.println(tmp);
		if(error!=null){
			error.printStackTrace();
		}
	}
	private static void stdout(String msg, Throwable error, Object... arg) {
		String tmp=String.format(msg,arg);
		System.out.println(tmp);
		error.printStackTrace();
	}
	
	public static void info(String msg,Object... arg) {
		if (logger != null) {
			logger.info(msg,arg);
		}else{
			System.out.println(EnvUtil.get(EnvUtil.projectName)+":"+msg);
			stdout(msg,null,arg);
		}
	}

	

	public static void info(String msg, Throwable error) {
		if (logger != null) {
			logger.info(msg, error);
		}else{
			stdout(msg,null);
		}
	}
	
	
	public static void info(String msg, Throwable error,Object... arg) {
		if (logger != null) {
			logger.info(msg, error);
		}else{
			stdout(msg,error,arg);
		}
	}

	public static void debug(String msg) {
		if (logger != null) {
			logger.debug(msg);
		}else{
			stdout(msg,null);
		}
	}

	public static void debug(String msg, Throwable error) {
		if (logger != null) {
			logger.debug(msg,error);
		}else{
			stdout(msg,null);
		}
	}
	
	public static void debug(String msg, Object... arg) {
		if (logger != null) {
			logger.debug(msg,arg);
		}else{
			stdout(msg,null,arg);
		}
	}
	
	public static void debug(String msg, Throwable error,Object... arg) {
		if (logger != null) {
			logger.debug(msg,error,arg);
		}else{
			stdout(msg,null,arg);
		}
	}

	public static void warn(String msg) {
		if (logger != null) {
			logger.warn(msg);
		}else{
			stdout(msg,null);
		}
	}

	public static void warn(String msg,Object... arg) {
		if (logger != null) {
			logger.warn(msg,arg);
		}else{
			stdout(msg,null,arg);
		}
	}
	
	public static void warn(String msg, Throwable error) {
		if (logger != null) {
			logger.warn(msg,error);
		}else{
			stdout(msg,error);
		}
	}

	public static void warn(String msg, Throwable error,Object... arg) {
		if (logger != null) {
			logger.warn(msg,error,arg);
		}else{
			stdout(msg,error,arg);
		}
	}
	
	public static void error(String msg, Throwable error) {
		if (logger != null) {
			logger.error(msg,error);
		}else{
			stdout(msg,error);
		}
	}
	
	public static void error(String msg, Throwable error,Object... arg) {
		if (logger != null) {
			logger.error(msg,arg);
		}else{
			stdout(msg,error,arg);
		}
	}

	public static void error(String msg) {
		if (logger != null) {
			logger.error(msg);
		}else{
			stdout(msg,null);
		}
	}
	
	public static void error(String msg,Object... arg) {
		if (logger != null) {
			logger.error(msg,arg);
		}else{
			stdout(msg,null,arg);
		}
	}

	public static void fatal(String msg) {
		if (logger != null) {
			logger.fatal(msg);
		}else{
			stdout(msg,null);
		}
	}
	
	public static void fatal(String msg,Object... arg) {
		if (logger != null) {
			logger.fatal(msg,arg);
		}else{
			stdout(msg,null,arg);
		}
	}

	public static void fatal(String msg, Throwable error) {
		if (logger != null) {
			logger.fatal(msg,error);
		}else{
			stdout(msg,error);
		}
	}
	
	public static void fatal(String msg, Throwable error,Object... arg) {
		if (logger != null) {
			logger.fatal(msg,error,arg);
		}else{
			stdout(msg,error,arg);
		}
	}
	

}
