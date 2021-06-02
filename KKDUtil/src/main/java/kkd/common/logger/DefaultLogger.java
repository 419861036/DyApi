package kkd.common.logger;


import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DefaultLogger implements ILogger {

	private Logger log = null;

	private static String formatMsg(String msg) {
		return msg;
	}

	public DefaultLogger() {
		//log.setLevel(Level.toLevel(""));
		this.log = Logger.getRootLogger();
	}
	
	public DefaultLogger(String log4jFileName) {
		try {
			InputStream is = DefaultLogger.class.getClassLoader().getResourceAsStream(log4jFileName);
			PropertyConfigurator.configure(is);
			this.log = Logger.getRootLogger();
		} catch (Exception e) {
			System.out.println("log4j file is not found:"+log4jFileName);
			LogWriter.error("",e);
		}
		
	}
	
	public DefaultLogger(Properties pt) {
		try {
//			  Properties pt = new Properties();
//		      pt.load(new StringBufferInputStream(content));
			PropertyConfigurator.configure(pt);
			this.log = Logger.getRootLogger();
		} catch (Exception e) {
			LogWriter.error("",e);
		}
	}

	@Override
	public void info(String msg) {
		this.log.info(formatMsg(msg));
	}

	@Override
	public void info(String msg, Throwable error) {
		this.log.info(formatMsg(msg), error);
	}

	@Override
	public void debug(String msg) {
		this.log.debug(formatMsg(msg));
	}

	@Override
	public void debug(String msg, Throwable error) {
		this.log.debug(formatMsg(msg), error);
	}

	@Override
	public void warn(String msg) {
		this.log.warn(formatMsg(msg));
	}

	@Override
	public void warn(String msg, Throwable error) {
		this.log.warn(formatMsg(msg), error);
	}

	@Override
	public void error(String msg, Throwable error) {
		this.log.error(formatMsg(msg), error);
	}

	@Override
	public void error(String msg) {
		this.log.error(formatMsg(msg));
	}

	@Override
	public void fatal(String msg) {
		this.log.fatal(formatMsg(msg));

	}

	@Override
	public void fatal(String msg, Throwable error) {
		this.log.fatal(formatMsg(msg), error);
	}

	@Override
	public void setLevel(String level) {
		this.log.setLevel(Level.toLevel(level));
	}

	@Override
	public void debug(String msg, Object... args) {
		String tmp=String.format(msg, args);
		this.log.debug(formatMsg(tmp));
	}

	@Override
	public void debug(String msg, Throwable error, Object... args) {
		String tmp=String.format(msg, args);
		this.log.debug(formatMsg(tmp),error);
	}

	@Override
	public void info(String msg, Object... args) {
		String tmp=String.format(msg, args);
		this.log.info(formatMsg(tmp));
	}

	@Override
	public void info(String msg, Throwable error, Object... args) {
		String tmp=String.format(msg, args);
		this.log.info(formatMsg(tmp),error);
	}

	@Override
	public void warn(String msg, Object... args) {
		String tmp=String.format(msg, args);
		this.log.warn(formatMsg(tmp));
	}

	@Override
	public void warn(String msg, Throwable error, Object... args) {
		String tmp=String.format(msg, args);
		this.log.warn(formatMsg(tmp),error);
	}

	@Override
	public void error(String msg, Object... args) {
		String tmp=String.format(msg, args);
		this.log.error(formatMsg(tmp));
	}

	@Override
	public void error(String msg, Throwable error, Object... args) {
		String tmp=String.format(msg, args);
		this.log.error(formatMsg(tmp),error);
	}

	@Override
	public void fatal(String msg, Object... args) {
		String tmp=String.format(msg, args);
		this.log.fatal(formatMsg(tmp));
	}

	@Override
	public void fatal(String msg, Throwable error, Object... args) {
		String tmp=String.format(msg, args);
		this.log.fatal(formatMsg(tmp),error);
	}

}
