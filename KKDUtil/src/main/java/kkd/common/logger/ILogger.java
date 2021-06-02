package kkd.common.logger;

public interface ILogger {
	
	public void debug(String msg);
	
	public void debug(String msg,Object... args);

	public void debug(String msg, Throwable error);
	
	public void debug(String msg, Throwable error,Object... args);

	public void info(String msg);
	
	public void info(String msg,Object... args);

	public void info(String msg, Throwable error);
	
	public void info(String msg, Throwable error,Object... args);

	public void warn(String msg);
	
	public void warn(String msg,Object... args);

	public void warn(String msg, Throwable error);
	
	public void warn(String msg, Throwable error,Object... args);

	public void error(String msg);
	
	public void error(String msg,Object... args);

	public void error(String msg, Throwable error);
	
	public void error(String msg, Throwable error,Object... args);

	public void fatal(String msg);
	
	public void fatal(String msg,Object... args);

	public void fatal(String msg, Throwable error);
	
	public void fatal(String msg, Throwable error,Object... args);
	
	public void setLevel(String level);
}
