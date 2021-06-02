package kkd.common.exception;

/**
 * 业务处理结果
 * @author Administrator
 *
 */
public class Result<T> {
	
	private String msg;
	private T value;
	private Boolean isException;
	private Exception e;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public Boolean getIsException() {
		return isException;
	}
	public void setIsException(Boolean isException) {
		this.isException = isException;
	}
	public Exception getE() {
		return e;
	}
	public void setE(Exception e) {
		this.e = e;
	}
	
}
